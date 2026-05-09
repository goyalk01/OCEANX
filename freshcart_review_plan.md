# FreshCart — Professional Review & Improvement Plan

## Overview
After a thorough audit of the entire FreshCart codebase (~40 files), I've identified **23 issues** ranging from crash-causing bugs to polish improvements. The work is divided into 4 sequential parts.

---

## Part 1: Critical Bug Fixes & Compilation Stabilization
> **Goal:** Make the project compile and run without crashes.

| # | File | Issue | Severity |
|---|------|-------|----------|
| 1 | `FreshCartApplication.kt` | **Duplicate `onCreate()` method** — won't compile | 🔴 BLOCKER |
| 2 | `LoginFragment.kt` | **Broken ViewModel instantiation** — `ViewModels` is a custom class that doesn't work with Fragment lifecycle | 🔴 BLOCKER |
| 3 | `CartRepositoryImpl.kt` | **`updateQuantity()` calls `.collect` inside a `suspend` function** — infinite coroutine hang / ANR | 🔴 CRASH |
| 4 | `MainActivity.kt` | **`CoroutineScope(Dispatchers.Main)` leaks** — not tied to Activity lifecycle | 🔴 CRASH |
| 5 | `HomeFragment.kt` | **`lifecycleScope` not imported** + uses deprecated `launchWhenStarted` | 🔴 BLOCKER |
| 6 | `CartFragment.kt` | Same as #5 | 🔴 BLOCKER |
| 7 | `CheckoutFragment.kt` | Same as #5 + `toast()` not imported | 🔴 BLOCKER |
| 8 | `CheckoutViewModel.kt` | **`UiState.Success(null as Order?)` unsafe cast** — runtime crash | 🟡 HIGH |
| 9 | `CartViewModel.kt` | **`getCartItemsSnapshot()` returns `emptyList()`** — placeholder never implemented | 🟡 HIGH |
| 10 | `HomeViewModel.kt` | **No ViewModelFactory** — constructor has dependencies, can't be instantiated with `ViewModelProvider(this).get()` | 🔴 CRASH |
| 11 | `CartViewModel.kt` | Same as #10 | 🔴 CRASH |
| 12 | `CheckoutViewModel.kt` | Same as #10 | 🔴 CRASH |
| 13 | `CategoryAdapter.kt` | **Uses `adapterPosition`** — deprecated, should use `bindingAdapterPosition` | 🟡 HIGH |

---

## Part 2: Data & Domain Layer Hardening
> **Goal:** Make the data pipeline correct, efficient, and interview-explainable.

| # | File | Issue | Severity |
|---|------|-------|----------|
| 14 | `CartDao.kt` | **Missing `updateQuantityByProductId` query** — forces inefficient read-map-write in repository | 🟠 MEDIUM |
| 15 | `CartRepositoryImpl.kt` | **Rewrite `updateQuantity` with direct DAO call** | 🟠 MEDIUM |
| 16 | `GetCartTotalUseCase.kt` | **Hardcoded magic numbers** (500, 10, 30) — should use `Constants` | 🟠 MEDIUM |
| 17 | `PlaceOrderUseCase.kt` | **Hardcoded "30 - 40 minutes"** — should use `Constants.ESTIMATED_DELIVERY_TIME` | 🟢 LOW |
| 18 | `CartMapper.kt` | Add idiomatic Kotlin extension functions alongside object methods | 🟢 LOW |
| 19 | `CheckoutViewModel.kt` + `LoginViewModel.kt` | **Duplicated phone validation logic** — extract to shared utility | 🟠 MEDIUM |

---

## Part 3: Presentation Layer & ViewModel Architecture
> **Goal:** Professional Fragment/ViewModel patterns, lifecycle safety, recruiter-impressive code.

| # | File | Issue | Severity |
|---|------|-------|----------|
| 20 | All Fragments | **None extend `BaseFragment`** — `BaseFragment` exists but is unused | 🟠 MEDIUM |
| 21 | `MainActivity.kt` | **Bottom nav visible on login screen** — should hide/show based on destination | 🟠 MEDIUM |
| 22 | `OrderSuccessFragment.kt` | **Uses raw `arguments?.getString()` instead of Safe Args** — Safe Args plugin is configured but unused | 🟠 MEDIUM |
| 23 | `ProductAdapter.kt` | **`getCartItem` suspend lambda never works** — needs architectural fix for cart state in product list | 🟠 MEDIUM |

---

## Part 4: Resource Polish & Final Hardening
> **Goal:** Production-quality XML, consistent UI, clean project structure.

| # | Area | Improvement |
|---|------|-------------|
| A | Layout XMLs | Fix hardcoded placeholder texts ("Product Name", "1 kg", "₹0") |
| B | `item_cart.xml` | Hardcoded `80dp` image size → use `@dimen/image_medium` |
| C | `fragment_order_success.xml` | Hardcoded `100dp` checkmark size → use dimens |
| D | `item_product.xml` | Hardcoded `"Product"` content description → use string resource |
| E | `bottom_nav_menu.xml` | Missing `android:icon` attributes |
| F | `item_category.xml` | Binding mismatch — layout has `categoryChip` but adapter references `categoryName` |
| G | Project root | Clean up downloaded gradle zips and old gradle versions |
| H | `proguard-rules.pro` | Add Room and Navigation rules |

---

## Execution Order
- ✅ **Part 1** → Execute now (this message)
- ⏳ **Part 2** → Next
- ⏳ **Part 3** → After Part 2
- ⏳ **Part 4** → Final pass
