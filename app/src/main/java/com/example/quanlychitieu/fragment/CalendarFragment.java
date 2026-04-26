package com.example.quanlychitieu.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlychitieu.R;
import com.example.quanlychitieu.adapter.CalendarDayAdapter;
import com.example.quanlychitieu.adapter.CalendarTransactionAdapter;
import com.example.quanlychitieu.database.TransactionDao;
import com.example.quanlychitieu.model.CalendarDayItem;
import com.example.quanlychitieu.model.TransactionWithCategory;
import com.example.quanlychitieu.preference.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarFragment extends Fragment {

    private TextView tvMonthTitle;
    private TextView tvMonthRange;
    private TextView tvIncomeTotal;
    private TextView tvExpenseTotal;
    private TextView tvBalanceTotal;
    private ImageView btnPrevMonth;
    private ImageView btnNextMonth;
    private RecyclerView rvCalendarDays;
    private RecyclerView rvTransactionGroups;

    private TransactionDao transactionDao;
    private Calendar displayCalendar;
    private int currentUserId;

    private final List<CalendarDayItem> dayItems = new ArrayList<>();
    private CalendarDayAdapter dayAdapter;
    private CalendarTransactionAdapter transactionAdapter;

    public CalendarFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transactionDao = new TransactionDao(requireContext());
        currentUserId = SessionManager.getCurrentUserId(requireContext());
        displayCalendar = Calendar.getInstance();

        initViews(view);
        setupRecyclerViews();
        setupEvents();
        loadCalendarData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCalendarData();
    }

    private void initViews(View view) {
        tvMonthTitle = view.findViewById(R.id.tvMonthTitle);
        tvMonthRange = view.findViewById(R.id.tvMonthRange);
        tvIncomeTotal = view.findViewById(R.id.tvIncomeTotal);
        tvExpenseTotal = view.findViewById(R.id.tvExpenseTotal);
        tvBalanceTotal = view.findViewById(R.id.tvBalanceTotal);
        btnPrevMonth = view.findViewById(R.id.btnPrevMonth);
        btnNextMonth = view.findViewById(R.id.btnNextMonth);
        rvCalendarDays = view.findViewById(R.id.rvCalendarDays);
        rvTransactionGroups = view.findViewById(R.id.rvTransactionGroups);
    }

    private void setupRecyclerViews() {
        dayAdapter = new CalendarDayAdapter(dayItems, new CalendarDayAdapter.OnDayClickListener() {
            @Override
            public void onDayClick(CalendarDayItem dayItem) {
                scrollToTransactionDate(dayItem);
            }

            @Override
            public void onDayDoubleClick(CalendarDayItem dayItem) {
                openTransactionFormForDate(dayItem);
            }
        });
        rvCalendarDays.setLayoutManager(new GridLayoutManager(requireContext(), 7));
        rvCalendarDays.setAdapter(dayAdapter);

        transactionAdapter = new CalendarTransactionAdapter(transaction -> {
            openEditTransactionForm(transaction);
        });
        rvTransactionGroups.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvTransactionGroups.setAdapter(transactionAdapter);
    }

    private void setupEvents() {
        btnPrevMonth.setOnClickListener(v -> {
            displayCalendar.add(Calendar.MONTH, -1);
            loadCalendarData();
        });

        btnNextMonth.setOnClickListener(v -> {
            displayCalendar.add(Calendar.MONTH, 1);
            loadCalendarData();
        });
    }

    private void loadCalendarData() {
        int month = displayCalendar.get(Calendar.MONTH) + 1;
        int year = displayCalendar.get(Calendar.YEAR);

        bindMonthHeader(month, year);
        bindMonthTotals(month, year);
        bindCalendarGrid(month, year);
        bindTransactionList(month, year);
    }

    private void bindMonthHeader(int month, int year) {
        tvMonthTitle.setText(String.format(Locale.getDefault(), "%02d/%d", month, year));

        Calendar start = Calendar.getInstance();
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, month - 1);
        start.set(Calendar.DAY_OF_MONTH, 1);

        Calendar end = Calendar.getInstance();
        end.set(Calendar.YEAR, year);
        end.set(Calendar.MONTH, month - 1);
        end.set(Calendar.DAY_OF_MONTH, start.getActualMaximum(Calendar.DAY_OF_MONTH));

        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM", Locale.getDefault());
        tvMonthRange.setText("(" + fmt.format(start.getTime()) + "–" + fmt.format(end.getTime()) + ")");
    }

    private void bindMonthTotals(int month, int year) {
        double income = transactionDao.getMonthTotalByType(currentUserId, month, year, "INCOME");
        double expense = transactionDao.getMonthTotalByType(currentUserId, month, year, "EXPENSE");
        double balance = income - expense;

        tvIncomeTotal.setText(formatMoney(income) + "đ");
        tvExpenseTotal.setText(formatMoney(expense) + "đ");
        tvBalanceTotal.setText((balance >= 0 ? "" : "-") + formatMoney(Math.abs(balance)) + "đ");
    }

    private void bindCalendarGrid(int month, int year) {
        Map<String, Double> expenseMap = transactionDao.getDailyExpenseMap(currentUserId, month, year);
        Map<String, Double> incomeMap = transactionDao.getDailyIncomeMap(currentUserId, month, year);

        dayItems.clear();

        Calendar firstDay = Calendar.getInstance();
        firstDay.set(Calendar.YEAR, year);
        firstDay.set(Calendar.MONTH, month - 1);
        firstDay.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = firstDay.get(Calendar.DAY_OF_WEEK);
        int offset = (firstDayOfWeek == Calendar.SUNDAY) ? 6 : firstDayOfWeek - 2;

        Calendar gridStart = (Calendar) firstDay.clone();
        gridStart.add(Calendar.DAY_OF_MONTH, -offset);

        for (int i = 0; i < 35; i++) {
            String dateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(gridStart.getTime());

            int day = gridStart.get(Calendar.DAY_OF_MONTH);
            boolean isCurrentMonth = gridStart.get(Calendar.MONTH) == (month - 1);

            double expense = expenseMap.containsKey(dateStr) ? expenseMap.get(dateStr) : 0;
            double income = incomeMap.containsKey(dateStr) ? incomeMap.get(dateStr) : 0;

            dayItems.add(new CalendarDayItem(dateStr, day, isCurrentMonth, expense, income));
            gridStart.add(Calendar.DAY_OF_MONTH, 1);
        }

        dayAdapter.notifyDataSetChanged();
    }

    private void bindTransactionList(int month, int year) {
        Map<String, List<TransactionWithCategory>> groupedData =
                transactionDao.getTransactionsGroupedByDate(currentUserId, month, year);

        transactionAdapter.setData(groupedData);
    }

    private String formatMoney(double amount) {
        return String.format(Locale.getDefault(), "%,.0f", amount);
    }
    private void scrollToTransactionDate(CalendarDayItem dayItem) {
        if (dayItem == null) {
            return;
        }

        double total = dayItem.getIncomeTotal() + dayItem.getExpenseTotal();
        if (total <= 0) {
            return;
        }

        int position = transactionAdapter.findHeaderPositionByDate(dayItem.getDate());
        if (position < 0) {
            return;
        }

        RecyclerView.LayoutManager layoutManager = rvTransactionGroups.getLayoutManager();
        if (layoutManager instanceof androidx.recyclerview.widget.LinearLayoutManager) {
            androidx.recyclerview.widget.LinearLayoutManager linearLayoutManager =
                    (androidx.recyclerview.widget.LinearLayoutManager) layoutManager;

            rvTransactionGroups.post(() ->
                    linearLayoutManager.scrollToPositionWithOffset(position, 0)
            );
        }
    }
    private void openTransactionFormForDate(CalendarDayItem dayItem) {
        if (dayItem == null || !dayItem.isCurrentMonth()) {
            return;
        }

        if (dayItem.getDate() == null || dayItem.getDate().trim().isEmpty()) {
            return;
        }

        TransactionFormFragment fragment =
                TransactionFormFragment.newInstance(dayItem.getDate(), true);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
    private void openEditTransactionForm(TransactionWithCategory transaction) {
        if (transaction == null) {
            return;
        }

        TransactionFormFragment fragment =
                TransactionFormFragment.newEditInstance(transaction.getId(), true);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}