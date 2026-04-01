# QUẢN LÝ CHI TIÊU CÁ NHÂN

## 1. Giới thiệu dự án
Đây là dự án môn **Lập trình di động** của nhóm, xây dựng ứng dụng **quản lý chi tiêu cá nhân** trên Android.

### Các use case chính
- Đăng ký
- Đăng nhập
- Thu nhập
- Chi tiêu
- Báo cáo
- Profile

---

## 2. Cấu trúc Git của nhóm

Nhóm sử dụng mô hình branch như sau:

- `main`
- `develop`
- `feature/auth`
- `feature/transaction`
- `feature/report-profile`

### Ý nghĩa từng branch

#### `main`
- Là nhánh ổn định nhất
- Dùng để demo hoặc nộp bài
- Không code trực tiếp trên nhánh này
- Chỉ nhận code từ `develop`

#### `develop`
- Là nhánh tích hợp chung của cả nhóm
- Sau khi một thành viên hoàn thành chức năng trên nhánh `feature`, code sẽ được merge vào `develop`
- Dùng để test toàn bộ ứng dụng sau khi ghép các phần của nhóm

#### `feature/auth`
- Là nhánh dành cho **thành viên 1**
- Phụ trách:
  - Đăng ký
  - Đăng nhập

#### `feature/transaction`
- Là nhánh dành cho **thành viên 2**
- Phụ trách:
  - Thu nhập
  - Chi tiêu

#### `feature/report-profile`
- Là nhánh dành cho **thành viên 3**
- Phụ trách:
  - Báo cáo
  - Profile

### Sơ đồ luồng branch

```text
feature/auth
feature/transaction       -> develop -> main
feature/report-profile
```

Nghĩa là:
1. Mỗi người code trên nhánh `feature` của mình
2. Khi chức năng chạy ổn thì merge vào `develop`
3. Khi toàn bộ ứng dụng ổn định thì merge `develop` vào `main`

---

## 3. Phân công branch cho từng thành viên

### Thành viên 1
- Branch làm việc: `feature/auth`
- Chức năng phụ trách:
  - Đăng ký
  - Đăng nhập

### Thành viên 2
- Branch làm việc: `feature/transaction`
- Chức năng phụ trách:
  - Thu nhập
  - Chi tiêu

### Thành viên 3
- Branch làm việc: `feature/report-profile`
- Chức năng phụ trách:
  - Báo cáo
  - Profile

---

## 4. Quy trình làm việc của nhóm

Mỗi thành viên chỉ làm việc trên nhánh `feature` của mình, không code trực tiếp trên `develop` hoặc `main`. Khi bắt đầu làm việc, thành viên phải chuyển sang nhánh `develop`, pull code mới nhất từ remote về máy, sau đó chuyển lại nhánh `feature` của mình và merge `develop` vào để cập nhật phần code mới nhất của cả nhóm. Sau khi cập nhật xong thì mới bắt đầu code. Trong quá trình làm, mỗi người cần commit với nội dung rõ ràng và push code thường xuyên lên đúng nhánh `feature` của mình để tránh mất dữ liệu. Khi hoàn thành một chức năng và đã tự test ổn định trên máy local, thành viên đó mới được merge nhánh `feature` vào `develop` để tích hợp với code chung của nhóm. Sau khi các chức năng trên `develop` đã hoạt động ổn định và toàn bộ ứng dụng đã được kiểm tra đầy đủ, nhóm mới merge `develop` vào `main` để tạo phiên bản hoàn chỉnh dùng cho demo hoặc nộp bài.

---

## 5. Cấu trúc thư mục của dự án

### Cấu trúc tổng quát

```text
app/
└── src/
    └── main/
        ├── java/com/example/quanlychitieu/
        │   ├── activity/
        │   ├── fragment/
        │   ├── adapter/
        │   ├── model/
        │   ├── database/
        │   ├── preference/
        │   ├── utils/
        │   └── listener/
        │
        ├── res/
        │   ├── layout/
        │   ├── drawable/
        │   ├── mipmap/
        │   ├── values/
        │   ├── menu/
        │   └── xml/
        │
        └── AndroidManifest.xml
```

---

## 6. Giải thích các thư mục của dự án

### `activity/`
Chứa các màn hình chính của ứng dụng.

Ví dụ:
- `LoginActivity.java`
- `RegisterActivity.java`
- `MainActivity.java`
- `AddTransactionActivity.java`
- `EditTransactionActivity.java`
- `ReportActivity.java`
- `ProfileActivity.java`

**Dùng để làm gì:**
- hiển thị giao diện từng màn hình
- xử lý sự kiện người dùng ở mức màn hình
- điều hướng giữa các màn hình

---

### `fragment/`
Chứa các Fragment nếu dự án sử dụng Fragment.

Ví dụ:
- `HomeFragment.java`
- `ReportFragment.java`
- `ProfileFragment.java`

**Dùng để làm gì:**
- tách giao diện thành từng phần nhỏ
- phù hợp khi dùng bottom navigation hoặc tab
- giúp code dễ quản lý hơn thay vì nhét hết vào 1 Activity

---

### `adapter/`
Chứa các Adapter cho RecyclerView hoặc ListView.

Ví dụ:
- `TransactionAdapter.java`
- `CategoryAdapter.java`
- `ReportAdapter.java`

**Dùng để làm gì:**
- đổ dữ liệu từ danh sách lên giao diện
- hiển thị item giao dịch, danh mục, báo cáo
- xử lý click item nếu cần

---

### `model/`
Chứa các class dữ liệu của dự án.

Ví dụ:
- `User.java`
- `Transaction.java`
- `Category.java`
- `Budget.java`

**Dùng để làm gì:**
- định nghĩa cấu trúc dữ liệu
- đại diện cho dữ liệu người dùng, giao dịch, danh mục, ngân sách
- dùng chung giữa Activity, Database, Adapter

---

### `database/`
Chứa phần xử lý database.

Ví dụ:
- `DBHelper.java`
- `UserDao.java`
- `TransactionDao.java`
- `CategoryDao.java`

**Dùng để làm gì:**
- tạo database
- tạo bảng dữ liệu
- thêm, sửa, xóa, lấy dữ liệu
- quản lý dữ liệu thu nhập, chi tiêu, người dùng

---

### `preference/`
Chứa phần SharedPreferences.

Ví dụ:
- `SessionManager.java`

**Dùng để làm gì:**
- lưu trạng thái đăng nhập
- lưu user hiện tại
- xử lý logout
- lưu các cấu hình nhỏ của ứng dụng

---

### `utils/`
Chứa các hàm dùng chung trong dự án.

Ví dụ:
- `Constants.java`
- `DateUtils.java`
- `CurrencyUtils.java`
- `ValidationUtils.java`

**Dùng để làm gì:**
- format ngày tháng
- format tiền tệ
- kiểm tra dữ liệu hợp lệ
- chứa hằng số dùng chung

---

### `listener/`
Chứa các interface callback nếu cần.

Ví dụ:
- `OnTransactionClickListener.java`

**Dùng để làm gì:**
- xử lý sự kiện click
- giao tiếp giữa Adapter và Activity/Fragment
- giúp code rõ ràng hơn

---

### `res/layout/`
Chứa các file giao diện XML.

Ví dụ:
- `activity_login.xml`
- `activity_register.xml`
- `activity_add_transaction.xml`
- `activity_report.xml`
- `item_transaction.xml`

**Dùng để làm gì:**
- định nghĩa giao diện cho từng màn hình
- định nghĩa giao diện cho từng item trong danh sách

---

### `res/drawable/`
Chứa ảnh, background, shape, icon custom.

**Dùng để làm gì:**
- tạo giao diện đẹp hơn
- chứa button background, bo góc, icon, ảnh minh họa

---

### `res/mipmap/`
Chứa icon chính của ứng dụng.

**Dùng để làm gì:**
- hiển thị icon app trên điện thoại

---

### `res/values/`
Chứa các file cấu hình tài nguyên.

Ví dụ:
- `strings.xml`
- `colors.xml`
- `themes.xml`
- `dimens.xml`

**Dùng để làm gì:**
- quản lý text dùng chung
- quản lý màu sắc
- quản lý theme
- quản lý kích thước giao diện

---

### `res/menu/`
Chứa file menu nếu có.

**Dùng để làm gì:**
- tạo menu trên toolbar
- tạo menu cho bottom navigation hoặc popup menu

---

### `res/xml/`
Chứa các file XML cấu hình khác.

**Dùng để làm gì:**
- cấu hình tùy chọn riêng cho app nếu cần

---

### `AndroidManifest.xml`
Là file cấu hình chính của ứng dụng Android.

**Dùng để làm gì:**
- khai báo Activity
- khai báo quyền của ứng dụng
- cấu hình tên app, icon, theme, launcher activity

---

## 7. Quy định đặt tên file

### File Java
Đặt tên đúng chức năng.

Ví dụ:
- `LoginActivity.java`
- `RegisterActivity.java`
- `TransactionAdapter.java`
- `SessionManager.java`

Không dùng:
- `test.java`
- `abc.java`
- `newactivity.java`

### File XML
Đặt tên theo chuẩn.

Ví dụ:
- `activity_login.xml`
- `activity_register.xml`
- `activity_profile.xml`
- `item_transaction.xml`

Không dùng:
- `manhinh1.xml`
- `abc.xml`
- `test_layout.xml`

---

## 8. Quy định dùng chung trong dự án

Để tránh lỗi khi ghép code, cả nhóm phải thống nhất:

### Kiểu giao dịch
Trong `Transaction`, trường `type` chỉ dùng:
- `INCOME`
- `EXPENSE`

### Tên bảng dữ liệu
Thống nhất các bảng:
- `users`
- `transactions`
- `categories`
- `budgets`

### Định dạng ngày
Thống nhất:
- `yyyy-MM-dd`

### Session đăng nhập
- quản lý bằng `SessionManager`
- không lưu lung tung ở nhiều chỗ

### Format tiền tệ
- thống nhất 1 kiểu hiển thị tiền Việt Nam
- không mỗi màn hình làm một kiểu khác nhau

---

## 9. Các file dễ conflict cần chú ý

Các file dưới đây rất dễ xảy ra conflict khi nhiều người cùng sửa:

- `AndroidManifest.xml`
- `build.gradle`
- `strings.xml`
- `colors.xml`
- `themes.xml`
- `DBHelper.java`
- `MainActivity.java`

### Quy định
- khi sửa file chung phải báo nhóm trước
- hạn chế nhiều người sửa cùng lúc
- pull `develop` mới nhất trước khi sửa

---

## 10. Quy định commit

### Commit phải rõ nghĩa
Ví dụ:
- `feat: add login screen`
- `feat: add register validation`
- `feat: add income transaction`
- `fix: correct report calculation`
- `ui: update profile screen`

### Không dùng commit message mơ hồ
Không dùng:
- `update`
- `fix bug`
- `aaa`
- `123`

---

## 11. Quy định merge

### Không merge vào `develop` khi:
- code chưa chạy
- chưa test
- còn lỗi cơ bản

### Không merge trực tiếp vào `main`
- `main` chỉ nhận code từ `develop`

### Trước khi merge vào `develop`
phải:
1. pull `develop` mới nhất
2. merge `develop` vào nhánh `feature`
3. xử lý conflict nếu có
4. test lại local
5. mới merge vào `develop`

---

## 12. Mục tiêu cuối cùng
- Source code rõ ràng, sạch, dễ đọc
- Mỗi thành viên có nhánh làm việc riêng
- Dễ quản lý pull/push/merge
- Hạn chế conflict
- Ứng dụng chạy ổn định trên máy thật
- Có thể build APK để demo hoặc nộp bài
