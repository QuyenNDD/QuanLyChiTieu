package com.example.quanlychitieu.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quanlychitieu.adapter.ReportPagerAdapter;
import com.example.quanlychitieu.R;
import com.example.quanlychitieu.database.ReportDao;
import com.example.quanlychitieu.preference.SessionManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import android.app.Activity;
import android.net.Uri;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import java.util.Calendar;

public class ReportFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TextView tvTotalIncome, tvTotalExpense, tvBalance;
    private TextView tvSelectDate;
    private ImageView imageView;
    private int selectedYear, selectedMonth;
    private int userId;
    private ReportDao reportDao;
    private androidx.activity.result.ActivityResultLauncher<Intent> createDocumentLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_report, container, false);

        createDocumentLauncher = registerForActivityResult(
                new androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                        android.net.Uri uri = result.getData().getData();
                        if (uri != null) {
                            // Tiến hành ghi dữ liệu vào Uri nhận được
                            writeCsvFile(uri);
                        }
                    }
                }
        );

        // Lấy userId từ SessionManager
        userId = SessionManager.getCurrentUserId(requireContext());

        reportDao = new ReportDao(requireContext());

        Calendar calendar = Calendar.getInstance();

        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);
        tvTotalExpense = view.findViewById(R.id.tvTotalExpense);
        tvBalance = view.findViewById(R.id.tvBalance);
        tabLayout = view.findViewById(R.id.tabLayoutReport);
        viewPager = view.findViewById(R.id.viewPagerReport);
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        tvSelectDate = view.findViewById(R.id.tvSelectDate);
        imageView = view.findViewById(R.id.ivCalendarIcon);

        tvSelectDate.setText(String.format("Tháng %02d/%d", (selectedMonth + 1), selectedYear));
        tvSelectDate.setOnClickListener(v -> showMonthYearPickerCustom());
        imageView.setOnClickListener(v -> showMonthYearPickerCustom());

        // Xuất file excel
        ImageButton btnExportExcel = view.findViewById(R.id.btnExportExcel);
        btnExportExcel.setOnClickListener(v -> {
            // Tạo tên file gợi ý theo định dạng: Bao_Cao_Thang_MM_YYYY.csv
            String fileName = String.format("Bao_Cao_Thang_%02d_%d.csv", (selectedMonth + 1), selectedYear);

            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("text/comma-separated-values"); // Định dạng file CSV
            intent.putExtra(Intent.EXTRA_TITLE, fileName);

            createDocumentLauncher.launch(intent);
        });

        ReportPagerAdapter adapter = new ReportPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText("CHI TIÊU");
                } else {
                    tab.setText("THU NHẬP");
                }
            }
        }).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                refreshChildFragments();
            }
        });

        updateReportData();

        return view;
    }

    private void showMonthYearPickerCustom() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_month_year_picker, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        TextView tvYear = view.findViewById(R.id.tvSelectedYear);
        ImageButton btnPrev = view.findViewById(R.id.btnPrevYear);
        ImageButton btnNext = view.findViewById(R.id.btnNextYear);
        GridView gvMonths = view.findViewById(R.id.gvMonths);

        String[] months = {"T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.item_month, months);
        gvMonths.setAdapter(adapter);

        tvYear.setText(String.valueOf(selectedYear));

        btnPrev.setOnClickListener(v -> {
            selectedYear--;
            tvYear.setText(String.valueOf(selectedYear));
        });

        btnNext.setOnClickListener(v -> {
            selectedYear++;
            tvYear.setText(String.valueOf(selectedYear));
        });

        gvMonths.setOnItemClickListener((parent, v, position, id) -> {
            selectedMonth = position;

            tvSelectDate.setText(String.format("Tháng %02d/%d", (selectedMonth + 1), selectedYear));

            dialog.dismiss();

            updateReportData();
        });

        dialog.show();
    }

    private void updateReportData() {
        if (reportDao == null) return;

        // Lấy lại userId mới nhất từ SessionManager
        userId = SessionManager.getCurrentUserId(requireContext());

        // --- TẦNG 2: CẬP NHẬT TỔNG QUÁT ---
        double income = reportDao.getTotalAmountByMonth(userId, selectedMonth + 1, selectedYear, "INCOME");
        double expense = reportDao.getTotalAmountByMonth(userId, selectedMonth + 1, selectedYear, "EXPENSE");
        double balance = income - expense;

        tvTotalIncome.setText(String.format("+%,.0fđ", income));
        tvTotalExpense.setText(String.format("-%,.0fđ", expense));
        tvBalance.setText(String.format("%,.0fđ", balance));

        // --- TẦNG 4: CẬP NHẬT BIỂU ĐỒ VÀ DANH SÁCH ---
        refreshChildFragments();
    }

    private void refreshChildFragments() {
        if (viewPager == null) return;

        Fragment currentFragment = getChildFragmentManager().findFragmentByTag("f" + viewPager.getCurrentItem());

        if (currentFragment instanceof ExpenseReportFragment) {
            ((ExpenseReportFragment) currentFragment).updateData(selectedMonth, selectedYear);
        } else if (currentFragment instanceof IncomeReportFragment) {
            ((IncomeReportFragment) currentFragment).updateData(selectedMonth, selectedYear);
        }
    }

    // Logic xuất file
    private void writeCsvFile(android.net.Uri uri) {
        try {
            android.os.ParcelFileDescriptor pfd = getContext().getContentResolver().openFileDescriptor(uri, "w");
            if (pfd == null) return;

            // Khởi tạo luồng ghi dữ liệu với encoding UTF-8
            java.io.FileOutputStream fos = new java.io.FileOutputStream(pfd.getFileDescriptor());
            java.io.OutputStreamWriter osw = new java.io.OutputStreamWriter(fos, java.nio.charset.StandardCharsets.UTF_8);
            java.io.BufferedWriter writer = new java.io.BufferedWriter(osw);

            // QUAN TRỌNG: Ghi mã BOM (\ufeff) lên đầu file để Excel trên PC đọc được Tiếng Việt
            writer.write("\ufeff");

            // 1. Ghi tiêu đề chung của file
            writer.write("BÁO CÁO THU CHI CHI TIẾT\n");
            writer.write("Thời gian: " + String.format("Tháng %02d/%d", (selectedMonth + 1), selectedYear) + "\n\n");

            // Đảm bảo cập nhật userId mới nhất trước khi lấy dữ liệu xuất file
            userId = SessionManager.getCurrentUserId(requireContext());

            // 2. LẤY DỮ LIỆU CHI TIÊU (EXPENSE) TỪ DAO (Đã sửa: Thêm tham số userId)
            java.util.List<com.example.quanlychitieu.model.ReportItem> expenseList = reportDao.getReportData(userId, selectedMonth, selectedYear, "EXPENSE");
            writer.write("I. KHOẢN CHI TIÊU\n");
            writer.write("Danh mục,Số tiền (đ),Tỷ lệ (%)\n"); // Header của bảng chi tiêu
            for (com.example.quanlychitieu.model.ReportItem item : expenseList) {
                writer.write(String.format("%s,%.0f,%.1f%%\n",
                        item.getCategoryName(), item.getAmount(), item.getPercentage()));
            }
            writer.write("\n");

            // 3. LẤY DỮ LIỆU THU NHẬP (INCOME) TỪ DAO (Đã sửa: Thêm tham số userId)
            java.util.List<com.example.quanlychitieu.model.ReportItem> incomeList = reportDao.getReportData(userId, selectedMonth, selectedYear, "INCOME");
            writer.write("II. KHOẢN THU NHẬP\n");
            writer.write("Danh mục,Số tiền (đ),Tỷ lệ (%)\n"); // Header của bảng thu nhập
            for (com.example.quanlychitieu.model.ReportItem item : incomeList) {
                writer.write(String.format("%s,%.0f,%.1f%%\n",
                        item.getCategoryName(), item.getAmount(), item.getPercentage()));
            }
            writer.write("\n");

            // 4. Ghi phần Tổng kết số dư ở cuối file (Đã sửa: Thêm tham số userId)
            double totalIncome = reportDao.getTotalAmountByMonth(userId, selectedMonth + 1, selectedYear, "INCOME");
            double totalExpense = reportDao.getTotalAmountByMonth(userId, selectedMonth + 1, selectedYear, "EXPENSE");
            writer.write("III. TỔNG KẾT CHUNG\n");
            writer.write(String.format("Tổng Thu Nhập,%.0f\n", totalIncome));
            writer.write(String.format("Tổng Chi Tiêu,%.0f\n", totalExpense));
            writer.write(String.format("Số Dư Còn Lại,%.0f\n", (totalIncome - totalExpense)));

            // Đóng luồng ghi dữ liệu
            writer.flush();
            writer.close();
            pfd.close();

            // Hiển thị thông báo thành công cho người dùng
            android.widget.Toast.makeText(getContext(), "Xuất báo cáo Excel thành công!", android.widget.Toast.LENGTH_SHORT).show();

        } catch (java.io.IOException e) {
            e.printStackTrace();
            android.widget.Toast.makeText(getContext(), "Lỗi: Không thể xuất file!", android.widget.Toast.LENGTH_SHORT).show();
        }
    }
}