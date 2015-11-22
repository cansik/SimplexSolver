# Simplex Solver
A simple simplex algorithm implemented in java.

### Example

**Inequations**

* `x1, x2 > 0`
* `x1 + x2 <= 40`
* `40x1 + 120x2 <= 2400`
* `7x1 + 12x2 <= 312`

* `100x1 + 250x2 = z -> Max`

```
Initial Schema:
y0 |        -1.00       -1.00       40.00
y1 |       -40.00     -120.00     2400.00
y2 |        -7.00      -12.00      312.00
z  |       100.00      250.00        0.00

Aim Column (0): 100.0
Pivot (0|0): -1.0
Step 0:
y0 |        -1.00       -1.00       40.00
y1 |        40.00      -80.00      800.00
y2 |         7.00       -5.00       32.00
z  |      -100.00      150.00     4000.00

Aim Column (1): 150.0
Pivot (2|1): -5.0
Step 1:
y0 |        -2.40        0.20       33.60
y1 |       -72.00       16.00      288.00
y2 |         1.40       -0.20        6.40
z  |       110.00      -30.00     4960.00

Aim Column (0): 110.0
Pivot (1|0): -72.0
Step 2:
y0 |         0.03       -0.33       24.00
y1 |        -0.01        0.22        4.00
y2 |        -0.02        0.11       12.00
z  |        -1.53       -5.56     5400.00

no new element found to switch!
Problem solved!
```
