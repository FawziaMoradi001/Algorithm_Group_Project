# 🌊 Flood Relief Supply Allocation Optimizer

**CSC4202 / CCS4202 — Design and Analysis of Algorithms | Group Project**

*Dynamic Programming (0/1 Knapsack) Approach*

[![Live Demo](https://img.shields.io/badge/🔴_LIVE_DEMO-View_Site-38bdf8?style=for-the-badge)](https://fawziamoradi001.github.io/Algorithm_Group_Project/)
![Java](https://img.shields.io/badge/Java-DP_Algorithm-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-Site-E34F26?style=flat-square&logo=html5&logoColor=white)
![GitHub Pages](https://img.shields.io/badge/GitHub_Pages-Deployed-222?style=flat-square&logo=githubpages)

<a href="https://fawziamoradi001.github.io/Algorithm_Group_Project/">
  <img src="images/banner.svg" alt="Flood Relief Supply Allocation Optimizer" width="100%">
</a>

<p align="center"><em>👆 Click the banner to open the live site</em></p>

**Group Name:** Relief Optimizer &nbsp;|&nbsp; Semester 2, 2025/2026 &nbsp;|&nbsp; Universiti Putra Malaysia

---

## 📖 The Scenario

**Kuala Krai, Kelantan — December 2014 Flood Crisis**

Kuala Krai is one of Southeast Asia's most flood-prone districts. In December 2014, the Kelantan River flooded catastrophically, displacing over 200,000 residents and cutting off roads, leaving relief agencies unable to reach every village at once.

A relief coordination center has **20 supply packages** ready (total weight **1,515 kg**) but only **one truck with a 1,000 kg capacity** — meaning roughly a third of the supplies must be left behind on this trip. The packages must reach **4 affected villages**: Kg. Manek Urai, Kg. Dabong, Kg. Bertam, and Kg. Laloh.

> **The core question:** which packages should be loaded onto the truck to maximize total humanitarian impact within the 1,000 kg limit?

| Metric | Value |
|---|---|
| Total supply weight | 1,515 kg |
| Truck capacity | 1,000 kg |
| Supply packages | 20 |
| Target villages | 4 |

<details>
<summary><strong>📦 View the full 20-package supply dataset</strong></summary>

| # | Package | Weight (kg) | Value | Category | Village |
|---|---|---|---|---|---|
| 1 | Rice (50kg×4) | 200 | 85 | Food | Kg. Manek Urai |
| 2 | Drinking Water (20L×10) | 200 | 90 | Water | Kg. Manek Urai |
| 3 | Medical Kit (Large) | 50 | 95 | Medical | Kg. Dabong |
| 4 | Baby Formula & Diapers | 30 | 80 | Baby Care | Kg. Dabong |
| 5 | Blankets & Sleeping Bags×20 | 60 | 55 | Shelter | Kg. Bertam |
| 6 | Portable Water Purifier | 25 | 88 | Water | Kg. Bertam |
| 7 | Canned Food (mixed box) | 150 | 70 | Food | Kg. Manek Urai |
| 8 | Tarpaulin Sheets×10 | 80 | 60 | Shelter | Kg. Laloh |
| 9 | Generator (small portable) | 120 | 65 | Equipment | Kg. Laloh |
| 10 | Antibiotics & First Aid | 20 | 92 | Medical | Kg. Dabong |
| 11 | Hygiene Kits×30 | 45 | 58 | Hygiene | Kg. Bertam |
| 12 | Instant Noodles (bulk) | 100 | 50 | Food | Kg. Laloh |
| 13 | Mosquito Nets×40 | 35 | 72 | Health | Kg. Manek Urai |
| 14 | Clothing Packs (assorted) | 70 | 40 | Clothing | Kg. Bertam |
| 15 | Cooking Equipment Set | 90 | 45 | Equipment | Kg. Laloh |
| 16 | Oral Rehydration Salts×100 | 15 | 78 | Medical | Kg. Dabong |
| 17 | Solar Lanterns×20 | 40 | 52 | Equipment | Kg. Manek Urai |
| 18 | Infant Medicine Kit | 10 | 82 | Medical | Kg. Dabong |
| 19 | Rubber Boats (inflatable×2) | 160 | 68 | Rescue | Kg. Laloh |
| 20 | Communication Radio Set | 15 | 75 | Communication | Kg. Dabong |

Total weight: 1,515 kg &nbsp;|&nbsp; Total value: 1,400 &nbsp;|&nbsp; Capacity: 1,000 kg

</details>

## 🧩 Model Formulation

Each of the *n* = 20 packages is modeled as a tuple `(name, weight, value, category, village)`, with a binary decision variable per package:

- `x_i = 1` → package *i* is loaded onto the truck
- `x_i = 0` → package *i* is left behind
- Packages **cannot be split** (0/1 variant — not fractional knapsack)

**Objective function** — maximize total humanitarian impact of the single truck trip:

```
Maximize:  Σ (value_i × x_i)   for i = 1 to n
subject to x_i ∈ {0, 1}
```

**Constraints:**

```
Weight:  Σ (weight_i × x_i) ≤ W = 1,000 kg
Binary:  x_i ∈ {0, 1}   — no fractional loading permitted
```

## ⚖️ Why Dynamic Programming? (Algorithm Selection)

Five candidate paradigms were evaluated before settling on DP:

| Algorithm | Time | Space | Verdict |
|---|---|---|---|
| **Dynamic Programming** ✅ | O(nW) | O(nW) | **Selected** — guarantees the optimal solution via tabulation over overlapping subproblems |
| Greedy (value/weight ratio) | O(n log n) | O(n) | Fast but **not optimal** — missed 5 relief-value points in testing |
| Brute Force (exhaustive) | O(2ⁿ) | O(n) | Optimal but **too slow** — 382 ms vs. DP's 3 ms; infeasible beyond ~25 items |
| Divide & Conquer | O(2ⁿ)* | O(n) | **Inefficient** — overlapping subproblems cause exponential blowup without memoization |
| Graph Algorithms | N/A | N/A | **Unsuitable** — no inherent graph structure in this allocation problem |

## 🧮 Algorithm Design

**Recurrence relation:**

```
dp[i][w] = max( dp[i-1][w],  value[i] + dp[i-1][w - weight[i]] )   if weight[i] ≤ w
dp[i][w] = dp[i-1][w]                                               otherwise

Base case: dp[0][w] = 0  for all w ≥ 0
```

**Table construction (pseudocode):**

```
FUNCTION SolveKnapsack(packages[], n, W):
    CREATE dp[0..n][0..W] = 0
    FOR i = 1 TO n:
        FOR w = 0 TO W:
            dp[i][w] = dp[i-1][w]
            IF packages[i].weight ≤ w:
                val = packages[i].value + dp[i-1][w - packages[i].weight]
                dp[i][w] = MAX(dp[i][w], val)
    RETURN dp[n][W]
```

**Traceback** (recovers *which* packages were selected, not just the optimal value):

```
FUNCTION Traceback(dp[][], packages[], n, W):
    selected = empty list
    w = W
    FOR i = n DOWNTO 1:
        IF dp[i][w] ≠ dp[i-1][w]:
            ADD i to selected
            w = w - packages[i].weight
    RETURN REVERSE(selected)
```

## ✅ Correctness — Proof by Induction

- **Base case (i = 0):** `dp[0][w] = 0` for all `w ≥ 0` — with zero items, no value is achievable. Trivially correct.
- **Inductive hypothesis:** assume `dp[i-1][w]` correctly gives the maximum relief value for items `1..i-1` at every capacity `0..W`.
- **Inductive step:** for item *i*, either it's excluded (`dp[i][w] = dp[i-1][w]`, correct by hypothesis) or included (`dp[i][w] = value[i] + dp[i-1][w-weight[i]]`, correct by hypothesis + optimal substructure). Taking the max of both cases makes `dp[n][W]` globally optimal. ∎
- **Termination:** the outer loop runs *n* times, the inner loop *W*+1 times — both finite, so the algorithm always halts.
- **Empirical verification:** an exhaustive brute-force search of all 2²⁰ = 1,048,576 subsets returns the *identical* result (value 1,162, weight 995 kg, same 16 items), confirming correctness independently of the proof.

## 📊 Complexity Analysis

| Algorithm | Time | Space | Ops (n=20) | Ops (n=100) | Optimal? |
|---|---|---|---|---|---|
| **Dynamic Programming** | Θ(nW) | Θ(nW) | 20,000 | 100,000 | ✅ Yes |
| Greedy | O(n log n) | O(n) | 86 | 664 | ❌ No |
| Brute Force | O(2ⁿ) | O(n) | 1,048,576 | 1.27 × 10³⁰ | ✅ Yes |

**Key insight:** DP occupies the practical sweet spot — the same optimal result as brute force, but pseudo-polynomial growth O(nW) instead of exponential O(2ⁿ). (Note: the Master Theorem doesn't apply here since this isn't a divide-and-conquer recurrence.)

## 🚀 Implementation

Built as a single Java file (`FloodReliefOptimizer.java`, JDK 21) that implements **all three algorithms on the same dataset** for direct comparison:

- **`SupplyPackage` class** — OOP model storing name, weight, value, category, and target village
- **DP table** — 2D array `dp[21][1001]`; each cell computed in O(1); traceback walks backward to recover the selected items
- **Timing** — `System.nanoTime()` used for precise execution measurement, with a formatted comparison table printed at the end

## 📈 Results Summary

| | Dynamic Programming | Greedy | Brute Force |
|---|---|---|---|
| **Total Relief Value** | **1,162** | 1,157 | **1,162** |
| Weight Used | 995 / 1,000 kg | 955 / 1,000 kg | 995 / 1,000 kg |
| Execution Time | 3.407 ms | 2.764 ms | 382.103 ms |
| Items Loaded | 16 / 20 | 17 / 20 | 16 / 20 |
| Optimal? | ✅ Yes | ❌ No | ✅ Yes |

> Greedy missed 5 relief-value points compared to DP — those 5 points represent real, undelivered oral rehydration salts and an infant medicine kit.

<details>
<summary><strong>🔢 Sample DP table (first 6 items, capacity 0–100)</strong></summary>

| i \ w | 0 | 10 | 20 | 30 | 40 | 50 | 60 | 70 | 80 | 90 | 100 |
|---|---|---|---|---|---|---|---|---|---|---|---|
| 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 1 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 2 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 | 0 |
| 3 | 0 | 0 | 0 | 0 | 0 | 95 | 95 | 95 | 95 | 95 | 95 |
| 4 | 0 | 0 | 0 | 80 | 80 | 95 | 95 | 95 | 175 | 175 | 175 |
| 5 | 0 | 0 | 0 | 80 | 80 | 95 | 95 | 95 | 175 | 175 | 175 |
| 6 | 0 | 0 | 0 | 88 | 88 | 95 | 168 | 168 | 183 | 183 | 183 |

Row 3 (Medical Kit, 50 kg, value 95) — value 95 first appears at capacity 50. Row 6 (Water Purifier, 25 kg, value 88) — combinations reach 183 by capacity 80+. The full table extends to row 20, capacity 1,000 → final optimal value: **1,162**.

</details>

<details>
<summary><strong>📦 Optimal selection — the 16 chosen packages</strong></summary>

| # | Package | Weight (kg) | Value | Category |
|---|---|---|---|---|
| 2 | Drinking Water (20L×10) | 200 | 90 | Water |
| 3 | Medical Kit (Large) | 50 | 95 | Medical |
| 4 | Baby Formula & Diapers | 30 | 80 | Baby Care |
| 6 | Portable Water Purifier | 25 | 88 | Water |
| 7 | Canned Food (mixed box) | 150 | 70 | Food |
| 8 | Tarpaulin Sheets×10 | 80 | 60 | Shelter |
| 9 | Generator (small portable) | 120 | 65 | Equipment |
| 10 | Antibiotics & First Aid | 20 | 92 | Medical |
| 11 | Hygiene Kits×30 | 45 | 58 | Hygiene |
| 13 | Mosquito Nets×40 | 35 | 72 | Health |
| 16 | Oral Rehydration Salts×100 | 15 | 78 | Medical |
| 17 | Solar Lanterns×20 | 40 | 52 | Equipment |
| 18 | Infant Medicine Kit | 10 | 82 | Medical |
| 20 | Communication Radio Set | 15 | 75 | Communication |
| 12 | Instant Noodles (bulk) | 100 | 50 | Food |

**Total: 1,162 value · 995 kg used (99.5% capacity utilization) · 16 of 20 items**

**Excluded (4 items):** #1 Rice (200 kg, v=85) · #14 Clothing (70 kg, v=40) · #15 Cooking Set (90 kg, v=45) · #19 Rubber Boats (160 kg, v=68)

</details>

## 🎓 What We Learned

- **Paradigms are tools, not exam topics** — Sorting, D&C, Greedy, and DP each have specific strengths. Knowing *when not to use* a paradigm turned out to be as valuable as knowing when to use it.
- **Optimality has real stakes** — Greedy's 5-point gap wasn't abstract: tracing it back showed a village going without oral rehydration salts or an infant medicine kit. In a humanitarian context, "close enough" isn't good enough.
- **Theory → bug-free code is its own skill** — our first implementation had an off-by-one error in the traceback that silently skipped item 1. Debugging it deepened our understanding of how DP table indices map back to the input array.
- **Cross-discipline connection** — the project tied together algorithms (CSC4202), discrete math (the induction proof), data structures (2D arrays, ArrayLists), and software engineering (OOP, Java) into one coherent solution.

**Future work:** multi-truck fleet allocation · time-dependent relief values · multi-objective optimization across villages.

## 📁 Repository Structure

```
Algorithm_Group_Project/
├── index.html                 # Live project write-up (design, analysis, walkthrough)
├── FloodReliefOptimizer.java  # Core DP algorithm implementation
└── images/                    # Team photos and README banner
```

## ▶️ Running the Program

```bash
javac FloodReliefOptimizer.java
java FloodReliefOptimizer
```

## 🌐 Live Site

**https://fawziamoradi001.github.io/Algorithm_Group_Project/**

## 👥 Team — "Relief Optimizer"

<table align="center">
  <tr>
    <td align="center" width="200">
      <img src="images/fawzia_avatar.png" width="120" height="120"><br>
      <b>Fawzia Moradi</b><br>
      <sub>226553</sub><br>
      Algorithm Design & Analysis
    </td>
    <td align="center" width="200">
      <img src="images/dawood_avatar.png" width="120" height="120"><br>
      <b>Dawood Nadeem</b><br>
      <sub>226920</sub><br>
      Java Implementation & Testing
    </td>
    <td align="center" width="200">
      <img src="images/zeng_avatar.png" width="120" height="120"><br>
      <b>Zeng Lixin</b><br>
      <sub>218617</sub><br>
      Portfolio & Presentation
    </td>
  </tr>
</table>

<p align="center"><em>Group Name: Relief Optimizer · CSC4202 · Semester 2, 2025/2026</em></p>

## 📚 References

- Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2009). *Introduction to Algorithms* (CLRS).
- Kleinberg, J., & Tardos, É. (2005). *Algorithm Design*.
- Kellerer, H., Pferschy, U., & Pisinger, D. (2004). *Knapsack Problems*.

---
*CSC4202 / CCS4202 — Design and Analysis of Algorithms · Semester 2, 2025/2026 · Universiti Putra Malaysia*
