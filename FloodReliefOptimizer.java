import java.util.*;

/**
 * ============================================================================
 * CSC4202 - Design and Analysis of Algorithm
 * GROUP PROJECT: Flood Relief Supply Allocation Optimizer
 * ============================================================================
 * 
 * SCENARIO: Kuala Krai, Kelantan - Post-Flood Disaster Relief
 * 
 * After severe monsoon flooding in Kelantan (inspired by the 2014 floods),
 * a relief coordination center must optimally allocate emergency supplies
 * to multiple affected villages using limited transport capacity.
 * 
 * ALGORITHM: Dynamic Programming (0/1 Knapsack Variant)
 * Each supply package can either be loaded or not (0/1 decision).
 * Goal: Maximize total relief value within the truck's weight capacity.
 * 
 * OPTIMIZATION FUNCTION:
 *   dp[i][w] = max(dp[i-1][w], dp[i-1][w - weight[i]] + value[i])
 * 
 * RECURRENCE RELATION:
 *   If weight[i] > w:  dp[i][w] = dp[i-1][w]
 *   Else:              dp[i][w] = max(dp[i-1][w], value[i] + dp[i-1][w - weight[i]])
 * 
 * Base case: dp[0][w] = 0 for all w
 * 
 * @author Group First Call
 */
public class FloodReliefOptimizer {

    // --- Data Structures ---

    /**
     * Represents a single relief supply package.
     */
    static class SupplyPackage {
        String name;
        int weight;      // in kg
        int reliefValue; // priority score (1-100) based on urgency & population served
        String category;
        String targetVillage;

        SupplyPackage(String name, int weight, int reliefValue, String category, String targetVillage) {
            this.name = name;
            this.weight = weight;
            this.reliefValue = reliefValue;
            this.category = category;
            this.targetVillage = targetVillage;
        }

        @Override
        public String toString() {
            return String.format("%-30s | %4d kg | Value: %3d | %-15s | %s",
                    name, weight, reliefValue, category, targetVillage);
        }
    }

    // --- Core DP Algorithm ---

    /**
     * Solves the 0/1 Knapsack problem using bottom-up Dynamic Programming.
     *
     * Time Complexity:  O(n * W) where n = number of items, W = capacity
     * Space Complexity: O(n * W) for the DP table
     *
     * @param packages  Array of supply packages
     * @param capacity  Maximum weight capacity of the transport (kg)
     * @return          The DP table for traceback
     */
    public static int[][] solveKnapsack(SupplyPackage[] packages, int capacity) {
        int n = packages.length;
        int[][] dp = new int[n + 1][capacity + 1];

        // Base case: dp[0][w] = 0 (no items, no value) - already 0 by default

        // Fill the DP table bottom-up
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                // Don't include item i
                dp[i][w] = dp[i - 1][w];

                // Include item i if it fits
                if (packages[i - 1].weight <= w) {
                    int includeValue = packages[i - 1].reliefValue + dp[i - 1][w - packages[i - 1].weight];
                    dp[i][w] = Math.max(dp[i][w], includeValue);
                }
            }
        }

        return dp;
    }

    /**
     * Traces back through the DP table to find which items were selected.
     *
     * @param dp        The completed DP table
     * @param packages  Array of supply packages
     * @param capacity  Maximum weight capacity
     * @return          List of selected package indices
     */
    public static List<Integer> traceback(int[][] dp, SupplyPackage[] packages, int capacity) {
        List<Integer> selected = new ArrayList<>();
        int n = packages.length;
        int w = capacity;

        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                selected.add(i - 1); // item was included
                w -= packages[i - 1].weight;
            }
        }

        Collections.reverse(selected);
        return selected;
    }

    // --- Comparison Algorithms ---

    /**
     * GREEDY APPROACH (for comparison):
     * Sort by value-to-weight ratio and greedily pick items.
     * Does NOT guarantee optimal solution for 0/1 Knapsack.
     *
     * Time Complexity: O(n log n) for sorting
     */
    public static List<Integer> greedySolve(SupplyPackage[] packages, int capacity) {
        int n = packages.length;
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) indices[i] = i;

        // Sort by value/weight ratio (descending)
        Arrays.sort(indices, (a, b) -> {
            double ratioA = (double) packages[a].reliefValue / packages[a].weight;
            double ratioB = (double) packages[b].reliefValue / packages[b].weight;
            return Double.compare(ratioB, ratioA);
        });

        List<Integer> selected = new ArrayList<>();
        int remainingCapacity = capacity;

        for (int idx : indices) {
            if (packages[idx].weight <= remainingCapacity) {
                selected.add(idx);
                remainingCapacity -= packages[idx].weight;
            }
        }

        Collections.sort(selected);
        return selected;
    }

    /**
     * BRUTE FORCE APPROACH (for comparison):
     * Try all 2^n subsets. Exponential time - only feasible for small n.
     *
     * Time Complexity: O(2^n)
     */
    public static List<Integer> bruteForceSolve(SupplyPackage[] packages, int capacity) {
        int n = packages.length;
        int bestValue = 0;
        List<Integer> bestSet = new ArrayList<>();

        for (int mask = 0; mask < (1 << n); mask++) {
            int totalWeight = 0;
            int totalValue = 0;
            List<Integer> current = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    totalWeight += packages[i].weight;
                    totalValue += packages[i].reliefValue;
                    current.add(i);
                }
            }

            if (totalWeight <= capacity && totalValue > bestValue) {
                bestValue = totalValue;
                bestSet = new ArrayList<>(current);
            }
        }

        return bestSet;
    }

    // --- Utility Methods ---

    /**
     * Calculate total weight of selected packages.
     */
    public static int totalWeight(SupplyPackage[] packages, List<Integer> selected) {
        int total = 0;
        for (int idx : selected) total += packages[idx].weight;
        return total;
    }

    /**
     * Calculate total relief value of selected packages.
     */
    public static int totalValue(SupplyPackage[] packages, List<Integer> selected) {
        int total = 0;
        for (int idx : selected) total += packages[idx].reliefValue;
        return total;
    }

    /**
     * Pretty-print results for a given solution.
     */
    public static void printSolution(String method, SupplyPackage[] packages,
                                      List<Integer> selected, int capacity) {
        System.out.println("\n" + "=".repeat(90));
        System.out.printf("  %s SOLUTION%n", method);
        System.out.println("=".repeat(90));
        System.out.printf("%-30s | %6s | %8s | %-15s | %s%n",
                "Supply Package", "Weight", "Value", "Category", "Target Village");
        System.out.println("-".repeat(90));

        for (int idx : selected) {
            System.out.println(packages[idx]);
        }

        int tw = totalWeight(packages, selected);
        int tv = totalValue(packages, selected);
        System.out.println("-".repeat(90));
        System.out.printf("TOTAL: %d/%d kg used (%.1f%% capacity) | Total Relief Value: %d%n",
                tw, capacity, (100.0 * tw / capacity), tv);
        System.out.printf("Items selected: %d/%d packages%n", selected.size(), packages.length);
    }

    // --- Complexity Analysis Display ---

    public static void printComplexityAnalysis(int n, int W) {
        System.out.println("\n" + "=".repeat(90));
        System.out.println("  ALGORITHM COMPLEXITY ANALYSIS");
        System.out.println("=".repeat(90));

        System.out.println("\n1. DYNAMIC PROGRAMMING (0/1 Knapsack):");
        System.out.printf("   Time Complexity:  O(n * W) = O(%d * %d) = O(%d)%n", n, W, (long) n * W);
        System.out.println("   Space Complexity: O(n * W)");
        System.out.println("   Best Case:   O(n * W) - always fills entire table");
        System.out.println("   Average Case: O(n * W) - same, table always fully computed");
        System.out.println("   Worst Case:  O(n * W) - same, pseudo-polynomial");
        System.out.println("   Optimality:  GUARANTEED OPTIMAL");

        System.out.println("\n2. GREEDY (Value/Weight Ratio):");
        System.out.printf("   Time Complexity:  O(n log n) = O(%d log %d)%n", n, n);
        System.out.println("   Space Complexity: O(n)");
        System.out.println("   Best Case:   O(n log n) - sorting dominates");
        System.out.println("   Average Case: O(n log n)");
        System.out.println("   Worst Case:  O(n log n)");
        System.out.println("   Optimality:  NOT GUARANTEED (heuristic only for 0/1 Knapsack)");

        System.out.println("\n3. BRUTE FORCE (All Subsets):");
        System.out.printf("   Time Complexity:  O(2^n) = O(2^%d) = O(%d)%n", n, (1 << Math.min(n, 30)));
        System.out.println("   Space Complexity: O(n)");
        System.out.println("   Best Case:   O(2^n) - must check all subsets");
        System.out.println("   Average Case: O(2^n)");
        System.out.println("   Worst Case:  O(2^n) - exponential, infeasible for large n");
        System.out.println("   Optimality:  GUARANTEED OPTIMAL (exhaustive search)");

        System.out.println("\n   CONCLUSION: DP provides optimal solution in pseudo-polynomial time,");
        System.out.println("   making it the best choice for this disaster relief optimization problem.");
    }

    // --- Main Program ---

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║     FLOOD RELIEF SUPPLY ALLOCATION OPTIMIZER                           ║");
        System.out.println("║     Kuala Krai, Kelantan - Post-Flood Disaster Relief                  ║");
        System.out.println("║     Algorithm: Dynamic Programming (0/1 Knapsack)                      ║");
        System.out.println("║     CSC4202 - Design and Analysis of Algorithm                         ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════╝");

        // --- Define the scenario data ---
        // Transport truck capacity
        int capacity = 1000; // kg

        // Relief supply packages available
        SupplyPackage[] packages = {
            new SupplyPackage("Rice (50kg sacks x4)",          200, 85, "Food",         "Kg. Manek Urai"),
            new SupplyPackage("Drinking Water (20L x10)",      200, 90, "Water",        "Kg. Manek Urai"),
            new SupplyPackage("Medical Kit (Large)",           50,  95, "Medical",      "Kg. Dabong"),
            new SupplyPackage("Baby Formula & Diapers",        30,  80, "Baby Care",    "Kg. Dabong"),
            new SupplyPackage("Blankets & Sleeping Bags x20",  60,  55, "Shelter",      "Kg. Bertam"),
            new SupplyPackage("Portable Water Purifier",       25,  88, "Water",        "Kg. Bertam"),
            new SupplyPackage("Canned Food (mixed box)",       150, 70, "Food",         "Kg. Manek Urai"),
            new SupplyPackage("Tarpaulin Sheets x10",          80,  60, "Shelter",      "Kg. Laloh"),
            new SupplyPackage("Generator (small portable)",    120, 65, "Equipment",    "Kg. Laloh"),
            new SupplyPackage("Antibiotics & First Aid",       20,  92, "Medical",      "Kg. Dabong"),
            new SupplyPackage("Hygiene Kits x30",              45,  58, "Hygiene",      "Kg. Bertam"),
            new SupplyPackage("Instant Noodles (bulk)",        100, 50, "Food",         "Kg. Laloh"),
            new SupplyPackage("Mosquito Nets x40",             35,  72, "Health",       "Kg. Manek Urai"),
            new SupplyPackage("Clothing Packs (assorted)",     70,  40, "Clothing",     "Kg. Bertam"),
            new SupplyPackage("Cooking Equipment Set",         90,  45, "Equipment",    "Kg. Laloh"),
            new SupplyPackage("Oral Rehydration Salts x100",   15,  78, "Medical",      "Kg. Dabong"),
            new SupplyPackage("Solar Lanterns x20",            40,  52, "Equipment",    "Kg. Manek Urai"),
            new SupplyPackage("Infant Medicine Kit",           10,  82, "Medical",      "Kg. Dabong"),
            new SupplyPackage("Rubber Boats (inflatable x2)",  160, 68, "Rescue",       "Kg. Laloh"),
            new SupplyPackage("Communication Radio Set",       15,  75, "Communication","Kg. Dabong"),
        };

        int n = packages.length;

        // --- Display all available packages ---
        System.out.println("\n" + "=".repeat(90));
        System.out.println("  AVAILABLE RELIEF SUPPLY PACKAGES");
        System.out.println("=".repeat(90));
        System.out.printf("%-30s | %6s | %8s | %-15s | %s%n",
                "Supply Package", "Weight", "Value", "Category", "Target Village");
        System.out.println("-".repeat(90));

        int totalAvailableWeight = 0;
        int totalAvailableValue = 0;
        for (SupplyPackage pkg : packages) {
            System.out.println(pkg);
            totalAvailableWeight += pkg.weight;
            totalAvailableValue += pkg.reliefValue;
        }
        System.out.println("-".repeat(90));
        System.out.printf("Total available: %d kg | Total value: %d | Truck capacity: %d kg%n",
                totalAvailableWeight, totalAvailableValue, capacity);
        System.out.printf("Must leave behind: %d kg worth of supplies%n",
                totalAvailableWeight - capacity);

        // --- Solve with Dynamic Programming ---
        System.out.println("\n>>> Solving with Dynamic Programming...");
        long dpStart = System.nanoTime();
        int[][] dp = solveKnapsack(packages, capacity);
        List<Integer> dpSelected = traceback(dp, packages, capacity);
        long dpTime = System.nanoTime() - dpStart;
        printSolution("DYNAMIC PROGRAMMING (OPTIMAL)", packages, dpSelected, capacity);
        System.out.printf("Execution time: %.3f ms%n", dpTime / 1_000_000.0);

        // --- Solve with Greedy ---
        System.out.println("\n>>> Solving with Greedy Algorithm...");
        long greedyStart = System.nanoTime();
        List<Integer> greedySelected = greedySolve(packages, capacity);
        long greedyTime = System.nanoTime() - greedyStart;
        printSolution("GREEDY (VALUE/WEIGHT RATIO)", packages, greedySelected, capacity);
        System.out.printf("Execution time: %.3f ms%n", greedyTime / 1_000_000.0);

        // --- Solve with Brute Force ---
        System.out.println("\n>>> Solving with Brute Force (may be slow for large n)...");
        long bfStart = System.nanoTime();
        List<Integer> bfSelected = bruteForceSolve(packages, capacity);
        long bfTime = System.nanoTime() - bfStart;
        printSolution("BRUTE FORCE (EXHAUSTIVE)", packages, bfSelected, capacity);
        System.out.printf("Execution time: %.3f ms%n", bfTime / 1_000_000.0);

        // --- Comparison Summary ---
        System.out.println("\n" + "=".repeat(90));
        System.out.println("  COMPARISON SUMMARY");
        System.out.println("=".repeat(90));
        System.out.printf("%-25s | %10s | %10s | %12s | %s%n",
                "Method", "Value", "Weight", "Time (ms)", "Optimal?");
        System.out.println("-".repeat(90));
        System.out.printf("%-25s | %10d | %7d kg | %9.3f ms | %s%n",
                "Dynamic Programming", totalValue(packages, dpSelected),
                totalWeight(packages, dpSelected), dpTime / 1_000_000.0, "YES");
        System.out.printf("%-25s | %10d | %7d kg | %9.3f ms | %s%n",
                "Greedy", totalValue(packages, greedySelected),
                totalWeight(packages, greedySelected), greedyTime / 1_000_000.0, "NO");
        System.out.printf("%-25s | %10d | %7d kg | %9.3f ms | %s%n",
                "Brute Force", totalValue(packages, bfSelected),
                totalWeight(packages, bfSelected), bfTime / 1_000_000.0, "YES");

        int greedyGap = totalValue(packages, dpSelected) - totalValue(packages, greedySelected);
        if (greedyGap > 0) {
            System.out.printf("%nGreedy missed %d relief value points compared to DP optimal!%n", greedyGap);
            System.out.println("This demonstrates why DP is superior for this problem.");
        } else {
            System.out.println("\nGreedy happened to find the optimal solution for this instance,");
            System.out.println("but this is NOT guaranteed in general.");
        }

        // --- Complexity Analysis ---
        printComplexityAnalysis(n, capacity);

        // --- DP Table Visualization (first few rows/cols) ---
        System.out.println("\n" + "=".repeat(90));
        System.out.println("  DP TABLE SAMPLE (first 6 items, capacity 0-100 in steps of 10)");
        System.out.println("=".repeat(90));
        System.out.printf("%3s |", "i\\w");
        for (int w = 0; w <= 100; w += 10) System.out.printf(" %4d", w);
        System.out.println();
        System.out.println("-".repeat(60));
        for (int i = 0; i <= Math.min(6, n); i++) {
            System.out.printf("%3d |", i);
            for (int w = 0; w <= 100; w += 10) System.out.printf(" %4d", dp[i][w]);
            System.out.println();
        }

        System.out.println("\n" + "=".repeat(90));
        System.out.println("  PROGRAM COMPLETED SUCCESSFULLY");
        System.out.println("  Optimal relief allocation has been determined.");
        System.out.println("=".repeat(90));
    }
}
