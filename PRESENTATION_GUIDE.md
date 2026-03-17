# FIESTA HOMES — PRESENTATION GUIDE & SCRIPT
## CSS122P Final Project — Real Estate Sales & Management System

---

## PART 1: MINIMUM REQUIREMENTS CHECKLIST

Before anything else — confirm all minimum requirements are MET:

| Requirement | How it's met in our system |
|---|---|
| 5 blocks, 20 lots each | AdminFrame enforces `block: 1–5` and `lot: 1–20` with validation before adding any property |
| Specify lot description (sqm), location, price | Admin "INJECT RECORD" form: HouseType dropdown, Price field, Block, Lot, SQM, Facing |
| Show list of lots based on user spec | CustomerMapFrame has filter sidebar: Block combo, Min SQM, Max Price → calls `filterProperties()` and highlights matching lots |
| Process selling / reservation | Customer submits reservation/purchase → goes to Agent queue → Agent approves or rejects |
| Generate report (all lots + status) | Admin clicks "GENERATE REPORT" → shows all lots: ID, Block, House Type, Status, Price with totals |

**All 5 requirements are satisfied. ✓**

---

## PART 2: PROGRAM FLOW (what to demo, in order)

### Step 1 — Login Screen (`LoginFrame`)
> "The program starts at the Login screen. There are three roles: Admin, Agent, and Customer. The system doesn't use passwords — you pick a role and enter directly. For Customers, a name prompt appears so the system can identify them."

- Show the 3 buttons: ADMIN (black/green), AGENT (blue), CUSTOMER (pink)
- Click CUSTOMER → show name input dialog

---

### Step 2 — Admin adds properties (`AdminFrame`)
> "The Admin is responsible for setting up the subdivision. They fill out the INJECT RECORD form on the right side."

**Demo steps:**
1. Login as Admin
2. Show the stats cards at the top: Total Inventory, Sold/Available, Revenue
3. On the right form:
   - Select a **HouseType** from dropdown (e.g. "Row House Bare") — note that Price and SQM auto-fill
   - Enter Block = 1, Lot = 1, Facing = NORTH → click ADD PROPERTY
   - Repeat for a few more lots (different blocks, types)
4. Show the table updating: ID (BLK1-L01), Block, House Type, SQM, Price, Status = AVAILABLE
5. Demo the FILTER: enter Block=1, click FILTER → only Block 1 shows
6. Click GENERATE REPORT → show the formatted text report with all lots and statuses

> "The Admin can add up to 100 lots (5 blocks × 20 lots). The system prevents invalid entries."

---

### Step 3 — Customer browses lots (`CustomerMapFrame`)
> "The Customer sees the subdivision as a visual grid. Each cell is one lot. Colors show status."

**Demo steps:**
1. Login as Customer (enter name e.g. "Juan dela Cruz")
2. Show the grid — available lots are white, sold are red-striped, reserved are gold-striped
3. Show the sidebar legend
4. Use the filter: select Block 1, enter Min SQM = 40, click SEARCH — non-matching lots dim out
5. Click an available lot → opens LotDetailFrame

---

### Step 4 — Lot Detail & Financial Terminal (`LotDetailFrame`)
> "Clicking a lot opens the detail screen. On the left is the lot info and house image. On the right is the Financial Terminal."

**Demo steps:**
1. Show left panel: Property ID, Base Price, House Type, SQM, Facing, Floor Area, Bedrooms
2. Show the house image (matches the HouseType)
3. On right: default is CASH mode → shows cost breakdown: Contract Price, Processing Fee, Reservation Fee, Balance
4. Switch to FINANCING → show BDO / PAG-IBIG / In-House radio buttons
5. Select BDO → adjust downpayment slider (20%) → adjust loan term slider (20 years)
   - Show monthly payment updating live
6. Select PAG-IBIG → show different rate (7.375% for Row House, 6.5% for Duplex)
7. Click **RESERVE** → confirm dialog → success message
   - Property is now RESERVED in the grid
8. Or click **INITIATE PURCHASE** → confirm → success

> "After submission, the transaction goes to the Agent's queue as PENDING."

---

### Step 5 — Agent approves transactions (`AgentFrame`)
> "The Agent is the one who approves or rejects customer transactions."

**Demo steps:**
1. Login as Agent
2. Show the ACTION QUEUE (left panel) — pending transaction cards appear
3. Each card shows: Property ID, Amount, Buyer name, Transaction type (RESERVATION/PURCHASE), Payment method
4. Click **APPROVE** → transaction moves to TRANSACTION HISTORY (right panel) as APPROVED
   - Property status becomes RESERVED or SOLD
5. Or click **REJECT** → confirmation dialog → property reverts to AVAILABLE
6. Click REFRESH to update the queue

---

## PART 3: CLASS RELATIONSHIPS — EXPLANATION SCRIPT

### Overview (say this first)
> "Our system is organized into 6 packages: `model`, `users`, `payment`, `enums`, `ui`, and the root `AppState`. Let me walk through each relationship."

---

### 1. INHERITANCE (Generalization) — User → Admin, Agent, Customer

**Where:** `User.java` is abstract. `Admin`, `Agent`, `Customer` all extend it.

```
User (abstract)
  ├── Admin
  ├── Agent
  └── Customer
```

**Script:**
> "`User` is our abstract base class. It holds common attributes: `id`, `name`, and `role`. It also declares an abstract method `login()`. Admin, Agent, and Customer all **inherit** from User — they get those common fields for free, and each one **overrides** `login()` with their own behavior. This is classic **Inheritance** and **Polymorphism** — same method name, different behavior per class."

**Key OOP terms to say:** Inheritance, Polymorphism, Abstract class, Method overriding

---

### 2. ENCAPSULATION — Property, Transaction, User

**Where:** All fields in `Property.java`, `Transaction.java`, `User.java` are `private`, accessed only through public getters (and limited setters).

**Script:**
> "All our model classes use **encapsulation**. For example, in `Property`, the fields like `price`, `status`, `sqm` are all `private`. You cannot modify them directly from outside. The only way to change the status is through `updateStatus(PropertyStatus)`, and the only way to change the price is through `setPrice(double)`. This protects the data from being accidentally changed in wrong places."

**Example to point to:** `Property.java` line 8–16 (private fields), line 33–39 (controlled setters)

---

### 3. AGGREGATION — Admin "has" Properties, Agent "has" Transactions

**Where:** `Admin` contains `ArrayList<Property>`. `Agent` contains `ArrayList<Transaction>`.

**Script:**
> "Admin and Agent use **aggregation** — a 'has-a' relationship. Admin has a list of Property objects. Agent has a list of Transaction objects. This is **aggregation** and not composition because the Property objects can exist independently — they're just referenced by Admin, not owned exclusively. If Admin were removed, the Properties would still logically exist."

**Diagram:** The UML shows a hollow diamond on the Admin/Agent side pointing to Property/Transaction.

---

### 4. ASSOCIATION — Transaction "uses" Property and PaymentStrategy

**Where:** `Transaction.java` holds a reference to a `Property` object and a `PaymentStrategy` object.

**Script:**
> "Transaction has an **association** with Property — it holds a reference to which specific lot is being transacted. It also holds a reference to the `PaymentStrategy` used. These are **associations** — Transaction uses these objects but doesn't own them in a parent-child sense."

---

### 5. REALIZATION / IMPLEMENTATION — PaymentStrategy (Strategy Design Pattern)

**Where:** `PaymentStrategy` is an `interface`. `CashPayment`, `BDOLoan`, `PagIbigLoan`, `InHouseFinancing` all implement it.

**Script:**
> "This is where our **Design Pattern** comes in — the **Strategy Pattern**. `PaymentStrategy` is an interface with two methods: `calculateMonthlyPayment()` and `getInterestRate()`. Each payment method is a separate class that **implements** this interface differently:
> - `CashPayment` returns 0 (no monthly, full upfront)
> - `BDOLoan` uses 7.25% annual rate with standard amortization formula
> - `PagIbigLoan` uses Circular 310 rates — 7.375% for properties below ₱750K, 6.5% for above
> - `InHouseFinancing` uses 12% — the highest rate
>
> In `LotDetailFrame`, the customer selects a payment option, and the system creates the right strategy object and passes it into `calculateMonthlyPayment()`. The calling code doesn't care which implementation — it just calls the interface method. This is the Strategy Pattern: **encapsulate a family of algorithms, make them interchangeable**."

**Diagram:** Dashed arrows (realization) from CashPayment, BDOLoan, PagIbigLoan, InHouseFinancing all pointing up to PaymentStrategy.

---

### 6. DEPENDENCY — AppState (Global State / Mediator)

**Where:** `AppState.java` holds static references to the single Admin, Agent, and current Customer.

**Script:**
> "`AppState` acts as a shared global state — like a simplified Singleton container. All the UI frames access `AppState.admin`, `AppState.agent`, and `AppState.currentCustomer` to get data and call methods. This is a **dependency** relationship — the UI frames depend on AppState to access the model objects. It keeps the system coordinated: when the customer submits a reservation, it calls `AppState.agent.addTransaction()`, so the Agent frame immediately sees it."

---

## PART 4: ENUMS — EXPLANATION SCRIPT

> "We use **enums** to represent fixed sets of constants that have specific meaning in our domain. Using enums instead of plain Strings prevents typos, makes comparisons safe, and lets the compiler catch invalid values."

### PropertyStatus
```java
enum PropertyStatus { AVAILABLE, RESERVED, SOLD }
```
> "A lot can only be in one of three states: Available (can be reserved/purchased), Reserved (pending agent approval), or Sold (fully completed). The enum ensures no one can set status to something invalid like 'AVAILABLE2' or 'sold'."

### TransactionType
```java
enum TransactionType { RESERVATION, PURCHASE }
```
> "A transaction is either a Reservation (just paying the reservation fee, pending) or a full Purchase. The Transaction class uses this to decide what status to set on the Property when it's completed."

### TransactionStatus
```java
enum TransactionStatus { PENDING, APPROVED, REJECTED }
```
> "Every transaction starts as PENDING when a customer submits it. The Agent either APPROVEs it (property becomes Reserved/Sold) or REJECTs it (property goes back to Available)."

### HouseType (special — enum with fields)
```java
enum HouseType {
    ROW_BARE("Row House Bare", 625_000, 40, 20, 1, 1, 5_000, 55_000),
    ROW_IMPROVED("Row House Improved", 625_000, 40, 30, 2, 1, 5_000, 55_000),
    ROW_IMPROVED_END("Row House Improved End Unit", 700_000, 40, 30, 2, 1, 5_000, 60_000),
    DUPLEX_2BR("Duplex 2 Bedrooms", 1_100_000, 55, 51, 2, 1, 10_000, 95_000),
    DUPLEX_3BR("Duplex 3 Bedrooms", 1_200_000, 55, 55, 3, 1, 10_000, 105_000);

    // each constant carries: displayName, basePrice, lotSqm, floorSqm,
    //                        bedrooms, bathrooms, reservationFee, processingFee
}
```
> "HouseType is our most powerful enum. Each house type constant carries all its data: the display name, base price, lot size, floor area, bedroom count, bathroom count, reservation fee, and processing fee. It also has a method `getPagIbigRate()` that returns the correct Pag-IBIG interest rate based on the price bracket — following Circular 310. This way, all type-specific data lives in one place and is always consistent."

---

## PART 5: OOP CONCEPTS SUMMARY TABLE

| OOP Concept | Where in our code |
|---|---|
| **Abstraction** | `User` abstract class — defines the concept of a system user without knowing the specific role. `PaymentStrategy` interface — defines what a payment method must do without specifying how. |
| **Encapsulation** | `Property`, `Transaction`, `User` — private fields, public getters, limited setters |
| **Inheritance** | `Admin`, `Agent`, `Customer` all extend `User` |
| **Polymorphism** | `login()` behaves differently for Admin, Agent, Customer. `calculateMonthlyPayment()` behaves differently for each payment strategy. |
| **Interface** | `PaymentStrategy` — defines a contract that all payment implementations must follow |

---

## PART 6: DESIGN PATTERN — STRATEGY

**Pattern:** Strategy Pattern
**Location:** `fiestaSystem.payment` package

**Intent:** Define a family of algorithms (payment calculation), encapsulate each one, and make them interchangeable.

**Components:**
- **Interface (Strategy):** `PaymentStrategy` — declares `calculateMonthlyPayment(double, int)` and `getInterestRate()`
- **Concrete Strategies:** `CashPayment`, `BDOLoan`, `PagIbigLoan`, `InHouseFinancing`
- **Context (uses the strategy):** `Customer.calculateMortgage()` and `LotDetailFrame.recalculate()` — both accept any `PaymentStrategy` object

**Why this pattern?**
> "Without the Strategy Pattern, we'd have a giant if-else block: if cash do this, if BDO do that, if Pag-IBIG do something else. That's hard to maintain and violates the Open/Closed Principle. With Strategy, we can add a new payment type (e.g. UnionBank Loan) by just creating a new class that implements `PaymentStrategy` — without touching any existing code."

---

## PART 7: DATA STORAGE

> "The project requirement allows arrays, lists, or databases. We use **ArrayLists** stored in memory:
> - `Admin` holds `ArrayList<Property>` — all 100 possible lots
> - `Agent` holds `ArrayList<Transaction>` — all reservation and purchase requests
>
> These persist for the duration of the program session. `AppState` acts as the central access point — all UI frames read and write through `AppState.admin` and `AppState.agent`."

---

## PART 8: QUICK Q&A PREP

**Q: Why is User abstract?**
> "Because a raw 'User' with no role doesn't make sense in our system. You can't be just a 'User' — you're either an Admin, Agent, or Customer. Declaring `login()` abstract forces each subclass to define its own login behavior."

**Q: What's the difference between aggregation and composition?**
> "Composition is a strong ownership — if the parent dies, the children die too (like a House and its Rooms). Aggregation is a weaker relationship — the child can exist without the parent. In our system, Property objects can exist independently of Admin; Admin just manages a list of them. So it's aggregation."

**Q: What if someone tries to reserve a lot that's already reserved?**
> "Both `Customer.submitReservation()` and `LotDetailFrame.reserveBtnActionPerformed()` check if `property.getStatus() == PropertyStatus.AVAILABLE` before proceeding. If not available, it shows an error dialog and does nothing."

**Q: How does the Strategy Pattern work exactly?**
> "The interface `PaymentStrategy` declares two methods. When the customer selects a payment type in LotDetailFrame, we create the correct object (e.g. `new BDOLoan()`) and call `calculateMonthlyPayment(principal, years)` on it. The UI doesn't know or care which implementation it is — it just calls the interface method. This is polymorphism through an interface."

**Q: What happens when an Agent rejects a transaction?**
> "In `Transaction.reject()`, the status is set to REJECTED, and then `property.updateStatus(PropertyStatus.AVAILABLE)` is called — so the property goes back to being available for other customers."

---

## PART 9: PACKAGE STRUCTURE (for the documentation)

```
fiestaSystem/
├── AppState.java              — Global state (Admin, Agent, current Customer)
├── TestRun.java               — Entry point / test
├── model/
│   ├── User.java              — Abstract base class for all users
│   ├── Property.java          — Represents a single lot/unit
│   └── Transaction.java       — Represents a sale or reservation record
├── users/
│   ├── Admin.java             — Manages properties, generates reports, filters
│   ├── Agent.java             — Manages transaction queue, approve/reject
│   └── Customer.java          — Browses lots, submits reservations/purchases
├── enums/
│   ├── HouseType.java         — 5 unit types with full specs and fees
│   ├── PropertyStatus.java    — AVAILABLE, RESERVED, SOLD
│   ├── TransactionType.java   — RESERVATION, PURCHASE
│   └── TransactionStatus.java — PENDING, APPROVED, REJECTED
├── payment/
│   ├── PaymentStrategy.java   — Interface (Strategy Pattern)
│   ├── CashPayment.java       — 0% rate, full upfront
│   ├── BDOLoan.java           — 7.25% annual
│   ├── PagIbigLoan.java       — 7.375% or 6.5% (Circular 310)
│   └── InHouseFinancing.java  — 12% annual
└── ui/
    ├── LoginFrame.java        — Role selection screen
    ├── AdminFrame.java        — Admin dashboard (table, filter, inject, report)
    ├── AgentFrame.java        — Agent dashboard (action queue, history)
    ├── CustomerMapFrame.java  — Buyer kiosk (visual lot grid, filter)
    └── LotDetailFrame.java    — Lot info + Financial Terminal
```

---

## PART 10: DEMO TIPS

1. **Run the program before presenting.** Make sure it compiles and runs in NetBeans.
2. **Start as Admin first** — add at least 10 properties across different blocks before demoing Customer.
3. **When showing the grid**, add lots from different blocks so the visual grid looks fuller.
4. **When showing the report**, make sure you have some RESERVED and SOLD lots so the totals are meaningful.
5. **Show the Pag-IBIG rate difference** — select a Row House (below ₱750K) then a Duplex (above ₱750K) and show the rate changes.
6. **If she asks about database**: say "We chose ArrayList as our data structure as permitted by the requirements. The data is maintained in-memory through the AppState class for the duration of the session."
