# 🌊 Flood Relief Supply Allocation Optimizer

**CSC4202 — Design and Analysis of Algorithms | Group Project**

A Dynamic Programming solution to a real-world disaster relief scenario: optimally allocating limited emergency supplies to flood-affected villages, inspired by the 2014 Kelantan floods in Kuala Krai, Malaysia.

[![Live Demo](https://img.shields.io/badge/🔴_LIVE_DEMO-View_Site-38bdf8?style=for-the-badge)](https://fawziamoradi001.github.io/Algorithm_Group_Project/)
![Java](https://img.shields.io/badge/Java-DP_Algorithm-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-Site-E34F26?style=flat-square&logo=html5&logoColor=white)
![GitHub Pages](https://img.shields.io/badge/GitHub_Pages-Deployed-222?style=flat-square&logo=githubpages)

<a href="https://fawziamoradi001.github.io/Algorithm_Group_Project/">
  <img src="images/banner.svg" alt="Flood Relief Supply Allocation Optimizer" width="100%">
</a>

<p align="center"><em>👆 Click the banner to open the live site</em></p>

---

## 📖 Overview

After severe monsoon flooding, a relief coordination center must decide which emergency supply packages to load onto a transport with limited weight capacity — maximizing total relief impact while staying within that limit. This is modeled and solved as a classic **0/1 Knapsack problem** using bottom-up Dynamic Programming.

## 🧮 Algorithm

- **Technique:** Dynamic Programming (0/1 Knapsack variant)
- **Recurrence relation:**
  ```
  dp[i][w] = dp[i-1][w]                                   if weight[i] > w
  dp[i][w] = max(dp[i-1][w], value[i] + dp[i-1][w-weight[i]])   otherwise
  ```
- **Time complexity:** O(n × W) — n = number of supply packages, W = truck capacity
- **Space complexity:** O(n × W) for the DP table

Each supply package has a weight (kg), a relief priority value (based on urgency and population served), a category, and a target village. The algorithm selects the combination of packages that maximizes total relief value without exceeding the transport's weight limit, then traces back through the DP table to report exactly which packages were chosen.

## 📁 Repository Structure

```
Algorithm_Group_Project/
├── index.html                 # Live project write-up (design, analysis, walkthrough)
├── FloodReliefOptimizer.java  # Core DP algorithm implementation
└── images/                    # Team member photos used on the site
```

## ▶️ Running the Program

```bash
javac FloodReliefOptimizer.java
java FloodReliefOptimizer
```

## 🌐 Project Website

The full write-up — including problem background, algorithm design, complexity analysis, sample results, and team reflections — is published via GitHub Pages:

**https://fawziamoradi001.github.io/Algorithm_Group_Project/**

## 👥 Team

| Name | Role |
|------|------|
| Fawzia Moradi | Algorithm Design & Analysis |
| Dawood Nadeem | Java Implementation & Testing |
| Zeng Lixin | Portfolio & Presentation |

---
*Course project for CSC4202 — Design and Analysis of Algorithms.*
