<div align="center">

# 💰 Cash Flow Minimizer System

### A Graph-Based Expense Settlement System Built with Java

<p>
  Simplify complex financial transactions using
  <b>Graphs</b>, <b>Greedy Algorithms</b>, and the
  <b>Java Collections Framework</b>.
</p>

![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=openjdk)
![Algorithm](https://img.shields.io/badge/Algorithm-Greedy-blue?style=for-the-badge)
![Data Structure](https://img.shields.io/badge/Data%20Structure-Graph-green?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Completed-brightgreen?style=for-the-badge)

</div>

---

## 📖 About the Project

The **Cash Flow Minimizer System** is a Java-based application designed to simplify financial settlements among multiple banks.

In a large transaction network, one bank may owe money to another bank while simultaneously being owed money by a third bank. Directly executing every original transaction can result in unnecessary transfers.

This project calculates the **net financial position** of every bank and generates a simplified set of settlement transactions.

The system also considers an additional real-world constraint:

> Different banks may support different payment modes.

If a debtor and creditor share a common payment mode, they can settle directly. Otherwise, the transaction is routed through a **World Bank**, which acts as an intermediary and supports all available payment modes.

---

## 🎯 Problem Statement

Consider the following transactions:

```text
Bank A → Bank B : ₹1000
Bank B → Bank C : ₹1000
