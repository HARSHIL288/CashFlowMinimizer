
# 💰 Cash Flow Minimizer System

### A Graph-Based Expense Settlement System Built with Java

Simplify complex financial transactions using **Graphs**, **Greedy
Algorithms**, and the **Java Collections Framework**.

![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=openjdk)
![Algorithm](https://img.shields.io/badge/Algorithm-Greedy-blue?style=for-the-badge)
![Data
Structure](https://img.shields.io/badge/Data%20Structure-Graph-green?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Completed-brightgreen?style=for-the-badge)
:::

------------------------------------------------------------------------

## 📖 About the Project

The **Cash Flow Minimizer System** is a Java-based application designed
to simplify financial settlements among multiple banks.

In a large transaction network, one bank may owe money to another bank
while simultaneously being owed money by a third bank. Directly
executing every original transaction can result in unnecessary
transfers.

This project calculates the **net financial position** of every bank and
generates a simplified set of settlement transactions.

The system also considers an additional real-world constraint:

> Different banks may support different payment modes.

If a debtor and creditor share a common payment mode, they can settle
directly. Otherwise, the transaction is routed through a **World Bank**,
which acts as an intermediary and supports all available payment modes.

------------------------------------------------------------------------

## 🎯 Problem Statement

Consider the following transactions:

``` text
Bank A → Bank B : ₹1000
Bank B → Bank C : ₹1000
```

Executing both transactions requires two separate payments. Since Bank B
receives and pays the same amount, its net balance is zero.

The settlement can therefore be simplified to:

``` text
Bank A → Bank C : ₹1000
```

### Before Optimization

``` text
Bank A ──₹1000──> Bank B ──₹1000──> Bank C
```

### After Optimization

``` text
Bank A ─────────────₹1000─────────────> Bank C
```

The goal of the project is to reduce unnecessary settlement transactions
while also respecting the payment modes supported by each bank.

------------------------------------------------------------------------

## ✨ Features

-   💸 Reduces unnecessary financial transactions between banks
-   🏦 Supports multiple participating banks
-   💳 Supports multiple payment modes for each bank
-   🌍 Uses a World Bank as an intermediary when required
-   📊 Represents original transactions using an adjacency matrix
-   ⚖️ Calculates the net debit or credit balance of every bank
-   🔍 Finds compatible payment modes between debtors and creditors
-   🧠 Uses a greedy settlement strategy
-   ⚡ Uses `PriorityQueue` for creditor selection
-   🌳 Uses `TreeSet` for payment mode storage and intersection
-   🗺️ Uses `HashMap` for efficient bank-name lookup
-   🖥️ Provides an interactive console-based interface

------------------------------------------------------------------------

## 🛠️ Tech Stack

  Category                   Technology
  -------------------------- -----------------------------
  **Programming Language**   Java
  **Algorithm**              Greedy Debt Settlement
  **Graph Representation**   Adjacency Matrix
  **Heap**                   PriorityQueue
  **Set**                    TreeSet
  **Map**                    HashMap
  **Input Handling**         Scanner
  **Programming Paradigm**   Object-Oriented Programming

------------------------------------------------------------------------

## 🧠 Core Algorithm

The system processes the transaction network in the following stages:

``` mermaid
flowchart TD
    A[Input Banks and Payment Modes] --> B[Input Original Transactions]
    B --> C[Build Transaction Graph]
    C --> D[Calculate Net Balance of Each Bank]
    D --> E[Select Largest Debtor]
    E --> F[Find Suitable Creditor]
    F --> G{Common Payment Mode?}
    G -->|Yes| H[Direct Settlement]
    G -->|No| I[Route Through World Bank]
    H --> J[Update Net Balances]
    I --> J
    J --> K{All Balances Zero?}
    K -->|No| E
    K -->|Yes| L[Print Final Transactions]
```

------------------------------------------------------------------------

## 🔍 How It Works

### 1️⃣ Register Banks

The user enters the number of banks participating in the transaction
network.

For every bank, the following information is provided:

-   Bank name
-   Number of supported payment modes
-   List of supported payment modes

Example:

``` text
World_Bank 2 Google_Pay PayTM
Bank_A 1 Google_Pay
Bank_B 1 PayTM
```

The first bank is treated as the **World Bank** and should support the
payment modes required to act as an intermediary.

------------------------------------------------------------------------

### 2️⃣ Input Transactions

Each original transaction is entered in the following format:

``` text
Debtor_Bank Creditor_Bank Amount
```

Example:

``` text
Bank_A Bank_B 500
```

This means:

``` text
Bank_A owes ₹500 to Bank_B
```

------------------------------------------------------------------------

### 3️⃣ Build the Transaction Graph

The transaction network is represented using an **adjacency matrix**:

``` java
int[][] graph = new int[numBanks][numBanks];
```

The value:

``` text
graph[i][j]
```

represents the amount that bank `i` owes bank `j`.

For example:

``` text
graph[1][2] = 500
```

means that Bank 1 owes Bank 2 an amount of ₹500.

------------------------------------------------------------------------

### 4️⃣ Calculate Net Balances

For every bank:

``` text
Net Amount = Total Incoming Money - Total Outgoing Money
```

The net amount determines the role of the bank:

  Net Amount   Meaning
  ------------ ----------------------------
  `> 0`        Bank should receive money
  `< 0`        Bank needs to pay money
  `= 0`        Bank has no remaining dues

Example:

``` text
Bank A → Bank B : ₹500
Bank B → Bank C : ₹300
```

The resulting net balances are:

``` text
Bank A = -500
Bank B = +200
Bank C = +300
```

Therefore:

-   Bank A must pay ₹500
-   Bank B must receive ₹200
-   Bank C must receive ₹300

------------------------------------------------------------------------

### 5️⃣ Select a Debtor

The system identifies a bank with a negative net amount.

The bank with the most negative balance represents the largest current
debtor.

Example:

``` text
Bank A = -500
Bank B = -200
Bank C = +700
```

The system selects **Bank A** first because it has the largest
outstanding debt.

------------------------------------------------------------------------

### 6️⃣ Find a Suitable Creditor

Banks with positive balances are potential creditors.

The implementation uses a Java `PriorityQueue` as a max heap:

``` java
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(
    (a, b) -> netAmounts[b].netAmount - netAmounts[a].netAmount
);
```

This allows creditors with larger positive balances to receive higher
priority.

------------------------------------------------------------------------

### 7️⃣ Check Payment Mode Compatibility

Each bank stores its supported payment modes in a `TreeSet<String>`.

Example:

``` text
Bank A → {Google_Pay, PayTM}
Bank B → {PayTM, PhonePe}
```

The common payment mode is:

``` text
PayTM
```

Therefore, the banks can settle directly:

``` text
Bank A ───── PayTM ─────> Bank B
```

------------------------------------------------------------------------

## 🌍 Role of the World Bank

Two banks may not always share a common payment mode.

Example:

``` text
Bank A → {Google_Pay}
Bank B → {PayTM}
```

A direct transaction is not possible through a shared payment method.

The system routes the settlement through the **World Bank**:

``` text
Bank A
   │
   │ Google_Pay
   ▼
World Bank
   │
   │ PayTM
   ▼
Bank B
```

The World Bank acts as a bridge between banks with incompatible payment
modes.

------------------------------------------------------------------------

## 🔄 Settlement Process

For a compatible debtor-creditor pair, the settlement amount is:

``` text
minimum(
    amount owed by debtor,
    amount receivable by creditor
)
```

Example:

``` text
Debtor   = -700
Creditor = +500
```

The transaction becomes:

``` text
Debtor pays Creditor ₹500
```

Updated balances:

``` text
Debtor   = -200
Creditor = 0
```

The creditor is now completely settled.

The process continues until:

``` text
Net Amount of Every Bank = 0
```

------------------------------------------------------------------------

## 🧩 Data Structures Used

### 📊 Adjacency Matrix

``` java
int[][] graph;
```

The adjacency matrix represents the original transaction network.

``` text
graph[i][j] = amount owed by bank i to bank j
```

### ⚡ PriorityQueue

``` java
PriorityQueue<Integer>
```

Used as a max heap to prioritize creditors with larger positive
balances.

### 🌳 TreeSet

``` java
TreeSet<String>
```

Used to:

-   Store payment modes
-   Avoid duplicate payment modes
-   Determine common payment methods

### 🗺️ HashMap

``` java
Map<String, Integer> indexOf = new HashMap<>();
```

Maps each bank name to its graph index.

Example:

``` text
World_Bank → 0
Bank_A     → 1
Bank_B     → 2
```

------------------------------------------------------------------------

## 🏗️ System Architecture

``` text
┌─────────────────────────────┐
│         User Input          │
│ Banks + Payment Modes       │
│ Original Transactions       │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│      Transaction Graph      │
│      Adjacency Matrix       │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│    Net Balance Calculation  │
│ Incoming - Outgoing Money   │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│    Greedy Settlement Engine │
│ Debtor ↔ Creditor Matching  │
└──────────────┬──────────────┘
               │
        ┌──────┴──────┐
        │             │
        ▼             ▼
  Common Mode     No Common Mode
        │             │
        ▼             ▼
 Direct Payment    World Bank
        │          Intermediary
        └──────┬──────┘
               │
               ▼
┌─────────────────────────────┐
│  Simplified Transaction List│
└─────────────────────────────┘
```

------------------------------------------------------------------------

## 📁 Project Structure

``` text
Cash-Flow-Minimizer/
│
├── CashFlowMinimizer.java
├── README.md
└── .gitignore
```

Compiled `.class` files are excluded using `.gitignore`.

------------------------------------------------------------------------

## 🚀 Getting Started

### Prerequisites

Make sure Java and the Java compiler are installed:

``` bash
java --version
javac --version
```

### Clone the Repository

``` bash
git clone https://github.com/HARSHIL288/Cash-Flow-Minimizer.git
cd Cash-Flow-Minimizer
```

If your repository uses a different name, replace the URL and directory
name accordingly.

### Compile

``` bash
javac CashFlowMinimizer.java
```

### Run

``` bash
java CashFlowMinimizer
```

------------------------------------------------------------------------

## 📥 Sample Input

``` text
5
World_Bank 2 Google_Pay PayTM
Bank_B 1 Google_Pay
Bank_C 1 Google_Pay
Bank_D 1 PayTM
Bank_E 1 PayTM
4
Bank_B World_Bank 300
Bank_C World_Bank 700
Bank_D Bank_B 500
Bank_E Bank_B 500
```

------------------------------------------------------------------------

## 📤 Output Format

The program displays settlement transactions in the following format:

``` text
<Debtor> pays Rs <Amount> to <Creditor> via <Payment_Mode>
```

Example:

``` text
Bank_A pays Rs 500 to Bank_B via Google_Pay
```

When a debtor and creditor do not have a compatible payment mode, the
settlement is routed through the World Bank.

------------------------------------------------------------------------

## ⏱️ Complexity

Let:

``` text
N = number of banks
```

The original transaction graph is represented using an `N × N` adjacency
matrix.

Therefore, the graph requires:

``` text
Space Complexity: O(N²)
```

The algorithm also performs repeated debtor selection, creditor
prioritization, and payment-mode compatibility checks. The exact running
time depends on:

-   Number of banks
-   Number of settlement iterations
-   Number of supported payment modes per bank

------------------------------------------------------------------------

## 💡 Concepts Demonstrated

This project demonstrates the practical application of:

-   Graph data structures
-   Greedy algorithms
-   Priority queues and heaps
-   Java Collections Framework
-   Object-Oriented Programming
-   Hash-based lookup
-   Set intersection
-   Adjacency matrix representation
-   Financial transaction modeling
-   Problem decomposition

------------------------------------------------------------------------

## 🔮 Future Enhancements

-   [ ] Build a graphical user interface
-   [ ] Create a web-based version
-   [ ] Add Spring Boot REST APIs
-   [ ] Add database persistence
-   [ ] Visualize the transaction graph
-   [ ] Add user authentication
-   [ ] Store transaction history
-   [ ] Export settlement reports
-   [ ] Add JUnit test cases
-   [ ] Support dynamic payment modes
-   [ ] Add transaction analytics

------------------------------------------------------------------------

## 👨‍💻 Author

::: {align="center"}
### Tirumareddy Harshil

**Computer Science & Engineering**\
**Vellore Institute of Technology**

[![GitHub](https://img.shields.io/badge/GitHub-HARSHIL288-black?style=for-the-badge&logo=github)](https://github.com/HARSHIL288)

[![LinkedIn](https://img.shields.io/badge/LinkedIn-harshil288-blue?style=for-the-badge&logo=linkedin)](https://www.linkedin.com/in/harshil288)

[![LeetCode](https://img.shields.io/badge/LeetCode-harshil__50288-orange?style=for-the-badge&logo=leetcode)](https://leetcode.com/u/harshil_50288/)
:::

------------------------------------------------------------------------

::: {align="center"}
### ⭐ If you found this project useful, consider giving the repository a star!
:::
