# FreshCart - Mini Grocery Delivery App

A production-grade Android grocery delivery application built in **100% Kotlin** following **Clean Architecture + MVVM** patterns. This project is designed to impress senior recruiters with enterprise-level code quality and best practices.

## 📋 Project Overview

**App Name:** FreshCart  
**Package:** `com.oceanx.freshcart`  
**Language:** Kotlin (100%)  
**Minimum SDK:** 21 (Android 5.0 Lollipop)  
**Target SDK:** 34 (Android 14)  
**Build System:** Gradle with Kotlin DSL  
**Architecture:** Clean Architecture + MVVM  
**UI Framework:** Material Design 3 (XML Layouts)

## 📸 App Showcase

**[🎥 Watch Full App Demo Video Here](https://youtube.com/shorts/placeholder)**

| Login Screen | Home Screen | Cart Screen | Checkout Screen | Order Success |
| :---: | :---: | :---: | :---: | :---: |
| <img src="https://via.placeholder.com/250x500.png?text=Login" width="180"/> | <img src="https://via.placeholder.com/250x500.png?text=Home" width="180"/> | <img src="https://via.placeholder.com/250x500.png?text=Cart" width="180"/> | <img src="https://via.placeholder.com/250x500.png?text=Checkout" width="180"/> | <img src="https://via.placeholder.com/250x500.png?text=Success" width="180"/> |

---

## 🏗️ Architecture Overview

### **Three-Layer Clean Architecture:**

```
┌─────────────────────────────────────────┐
│       PRESENTATION LAYER (UI)           │
│  Fragments, ViewModels, Adapters        │
├─────────────────────────────────────────┤
│       DOMAIN LAYER (Business Logic)     │
│  Use Cases, Interfaces, Domain Models   │
├─────────────────────────────────────────┤
│       DATA LAYER (Persistence)          │
│  Room Database, DAOs, Repositories      │
└─────────────────────────────────────────┘
```

### **Design Principles:**

- **Dependency Inversion:** Outer layers depend on inner layers; inner layers never depend on outer
- **Reactive Programming:** Uses Kotlin Flows for real-time data updates
- **Single Responsibility:** Each class has one reason to change
- **DRY (Don't Repeat Yourself):** Code reuse through extensions and utilities
- **Zero Business Logic in UI:** All logic in ViewModels and Use Cases

---

## 📁 Project Structure

```
app/src/main/
├── java/com/oceanx/freshcart/
│   ├── FreshCartApplication.kt              ← App initialization
│   ├── data/
│   │   ├── local/
│   │   │   ├── AppDatabase.kt               ← Room database singleton
│   │   │   ├── dao/CartDao.kt               ← Database queries
│   │   │   └── entity/CartItemEntity.kt     ← Database table definition
│   │   ├── model/                           ← Domain models (pure Kotlin)
│   │   │   ├── Product.kt
│   │   │   ├── CartItem.kt
│   │   │   ├── Category.kt
│   │   │   └── Order.kt
│   │   ├── mapper/CartMapper.kt             ← Entity ↔ Domain mapping
│   │   ├── repository/CartRepositoryImpl.kt  ← Data layer implementation
│   │   └── source/ProductDataSource.kt      ← Fake product data (20+ items)
│   ├── domain/
│   │   ├── repository/CartRepository.kt     ← Interface/contract
│   │   └── usecase/
│   │       ├── AddToCartUseCase.kt
│   │       ├── RemoveFromCartUseCase.kt
│   │       ├── UpdateCartQuantityUseCase.kt
│   │       ├── GetCartItemsUseCase.kt
│   │       ├── GetCartTotalUseCase.kt
│   │       ├── ClearCartUseCase.kt
│   │       └── PlaceOrderUseCase.kt
│   ├── presentation/
│   │   ├── MainActivity.kt                  ← Single activity host
│   │   ├── common/
│   │   │   ├── UiState.kt                   ← Loading/Success/Error states
│   │   │   └── BaseFragment.kt              ← Fragment base class
│   │   ├── login/
│   │   │   ├── LoginFragment.kt
│   │   │   └── LoginViewModel.kt
│   │   ├── home/
│   │   │   ├── HomeFragment.kt
│   │   │   ├── HomeViewModel.kt
│   │   │   └── adapter/
│   │   │       ├── ProductAdapter.kt        ← Uses DiffUtil
│   │   │       └── CategoryAdapter.kt
│   │   ├── cart/
│   │   │   ├── CartFragment.kt
│   │   │   ├── CartViewModel.kt
│   │   │   └── adapter/CartAdapter.kt       ← Uses DiffUtil
│   │   ├── checkout/
│   │   │   ├── CheckoutFragment.kt
│   │   │   └── CheckoutViewModel.kt
│   │   └── ordersuccess/
│   │       └── OrderSuccessFragment.kt
│   └── utils/
│       ├── Constants.kt                     ← App-wide constants
│       ├── Extensions.kt                    ← Kotlin extension functions
│       ├── CurrencyFormatter.kt
│       └── OrderIdGenerator.kt
├── res/
│   ├── layout/
│   │   ├── activity_main.xml
│   │   ├── fragment_login.xml
│   │   ├── fragment_home.xml
│   │   ├── fragment_cart.xml
│   │   ├── fragment_checkout.xml
│   │   ├── fragment_order_success.xml
│   │   ├── item_product.xml
│   │   ├── item_category.xml
│   │   └── item_cart.xml
│   ├── navigation/nav_graph.xml             ← Navigation structure
│   ├── menu/bottom_nav_menu.xml
│   ├── drawable/
│   │   ├── bg_button_primary.xml
│   │   ├── bg_category_selector.xml
│   │   └── ic_launcher_foreground.xml
│   ├── anim/
│   │   ├── slide_in_right.xml
│   │   ├── slide_out_left.xml
│   │   └── fade_in.xml
│   └── values/
│       ├── colors.xml                      ← Material Design 3 palette
│       ├── strings.xml                     ← All UI text (localization ready)
│       ├── dimens.xml                      ← Spacing & sizes
│       └── themes.xml                      ← Material 3 theme
├── AndroidManifest.xml
├── build.gradle.kts
└── settings.gradle.kts
```

---

## 🎯 5 User-Facing Screens

### **1. Login Screen (`LoginFragment`)**
- Phone number input (10-digit validation)
- OTP verification (fake OTP: `1234`)
- Smooth `animateLayoutChanges` transitions
- `SharedPreferences` login persistence

### **2. Home Screen (`HomeFragment`)**
- Category horizontal scrolling with dynamic filtering
- 20+ products across 8 categories (All, Fruits, Vegetables, Dairy, Bakery, Beverages, Snacks, Grains)
- Real-time search with `debounce(300ms)`
- 2-column product grid using `GridLayoutManager`
- Cart badge showing item count
- Add to cart button or quantity controls

### **3. Shopping Cart (`CartFragment`)**
- Quantity +/- controls with instant updates via Room Flow
- Bill summary with:
  - MRP Total
  - 10% discount if total > ₹500
  - FREE delivery if subtotal > ₹500, else ₹30
  - Final total
- Swipe-to-delete with undo snackbar
- Empty state with "Browse Products" button

### **4. Checkout (`CheckoutFragment`)**
- Multi-field address form with inline validation
- Name, phone, flat number, area, city, pincode
- Payment method selection (COD / Online)
- Order summary expandable card
- "Place Order" with 1.5s loading simulation

### **5. Order Success (`OrderSuccessFragment`)**
- Animated checkmark
- Order details display
- Order ID: `ORD#XXXX` (randomly generated)
- Estimated delivery: 30-40 minutes
- Delivery progress visualization
- "Continue Shopping" clears cart and goes to Home

---

## 💾 Data Management

### **Room Database:**
- **Local persistence** for cart items
- **Automatic migrations** (with fallback)
- **Reactive queries** returning `Flow<>`
- **Efficient updates** only changed rows redraw

### **Fake Product Data:**
```
ProductDataSource provides 25+ products including:
- Fruits: Banana (₹40), Apple (₹120), Mango (₹80)
- Vegetables: Tomato (₹30), Onion (₹25), Potato (₹20)
- Dairy: Milk (₹60), Paneer (₹85), Curd (₹45)
- And more...
```

---

## 🚀 Key Features

### **Architectural Excellence:**
✅ Clean Architecture with strict layer separation  
✅ MVVM pattern with reactive StateFlow/LiveData  
✅ Dependency Inversion Principle  
✅ Single Activity + Navigation Component  
✅ Zero business logic in Fragments  

### **Code Quality:**
✅ 100% Kotlin (no Java)  
✅ KDoc comments on every public method  
✅ No hardcoded strings (all in `strings.xml`)  
✅ DiffUtil for efficient RecyclerView updates  
✅ Coroutines with proper scope management  

### **UI/UX:**
✅ Material Design 3 theme (Blue + White)  
✅ Smooth fragment animations  
✅ Real-time search with debounce  
✅ Cart badge with item count  
✅ Proper error handling & validation  

### **Production-Ready:**
✅ Proper error messages  
✅ Loading states (UiState sealed class)  
✅ Empty states with helpful CTAs  
✅ Swipe-to-delete with undo  
✅ Form validation with inline errors  

---

## 📦 Dependencies

All dependencies are defined in `build.gradle.kts`:

```kotlin
// Core
androidx.core:core-ktx:1.12.0
androidx.appcompat:appcompat:1.6.1

// Architecture
androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0
androidx.navigation:navigation-fragment-ktx:2.7.6

// Database
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1

// Async
org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3

// UI
com.google.android.material:material:1.11.0
androidx.recyclerview:recyclerview:1.3.2

// Image Loading
com.github.bumptech.glide:glide:4.16.0
```

---

## 🔐 Authentication

**Login Flow:**
1. User enters 10-digit phone number
2. Validates: Must be 10 digits, start with 6/7/8/9
3. Taps "Send OTP" → OTP input section appears
4. Enters fake OTP `1234` → Saves to `SharedPreferences`
5. On app reopen: Checks `SharedPreferences` → Redirects to Home if logged in

**Logout:** User's login state persists until app is reinstalled

---

## 💳 Order Processing

**Cart Calculation:**
```
MRP Total = Sum of all item prices × quantities
Discount = If MRP > ₹500: 10%, else: ₹0
Delivery = If (MRP - Discount) > ₹500: FREE, else: ₹30
Final Total = MRP - Discount + Delivery Fee
```

**Order Placement:**
1. User fills delivery address
2. Selects payment method
3. Taps "Place Order"
4. Shows loading for 1.5 seconds (simulated API call)
5. Generates Order ID: `ORD#XXXX`
6. Clears cart via `ClearCartUseCase`
7. Navigates to success screen

---

## 🛠️ Build & Run

### **Prerequisites:**
- Android Studio Electric Eel or later
- JDK 11+
- Kotlin 1.9.22

### **Build:**
```bash
./gradlew build
```

### **Run on Emulator:**
```bash
./gradlew installDebug
adb shell am start -n com.oceanx.freshcart/.presentation.MainActivity
```

### **Sync Dependencies:**
```bash
./gradlew sync
```

---

## 📱 Testing the App

1. **Login:**
   - Phone: `9876543210`
   - OTP: `1234`

2. **Browse Products:**
   - Tap category chips to filter
   - Use search bar to find items
   - See real-time cart badge update

3. **Add to Cart:**
   - Tap "+" on any product
   - See quantity controls appear
   - Swipe left to remove (with undo)

4. **Checkout:**
   - Fill delivery address
   - Select payment method
   - See bill summary
   - Tap "Place Order"

5. **Order Success:**
   - See order confirmation
   - Tap "Continue Shopping"
   - Cart is cleared, back to Home

---

## 👨‍💼 Professional Considerations

This codebase demonstrates:
- **Clean Architecture mastery** → Separates concerns perfectly
- **Kotlin expertise** → Extension functions, sealed classes, coroutines
- **Android best practices** → Navigation, ViewModel scoping, Room, etc.
- **Production mindset** → Error handling, validation, loading states
- **Attention to detail** → All strings externalized, proper spacing, consistent naming

---

## 📄 File Statistics

- **Kotlin Files:** 30+
- **Layout XMLs:** 10+
- **Resource Files:** 5 (colors, strings, dimens, themes, drawables)
- **Lines of Code:** ~3,500+ (production-quality, well-commented)
- **Total Files:** 50+

---

## 🎓 Learning Resources

This project teaches:
- Clean Architecture layering
- MVVM pattern implementation
- Room database integration
- Coroutines and Flow
- Navigation Component
- RecyclerView with DiffUtil
- Material Design 3
- Kotlin best practices

---

## 📝 Author

**Krish Goyal**  
OceanX Agency Internship  
May 2026

---

## Git Remote

Repository URL: https://github.com/goyalk01/OCEANX.git

### Push instructions
Open a terminal in the project root and run:

```bash
git init
git add .
git commit -m "Initial import: FreshCart app"
git remote add origin https://github.com/goyalk01/OCEANX.git
git branch -M main
git push -u origin main
```

If the remote is private, authenticate when prompted (use a PAT if required).

---

## 📞 Support

For questions about the architecture or code:
1. Review the KDoc comments in each file
2. Check `Constants.kt` for app-wide configuration
3. See `domain/usecase/` for business logic examples
4. Check `presentation/common/UiState.kt` for state management pattern

---

**Built with ❤️ for production-grade Android development.**
