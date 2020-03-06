import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("output.csv", index_col = "Année")
ax = df.plot(logy=True, title="Simulation 4 (Population initiale: 6000)", marker='s', markersize = 3, linewidth=1)
ax.set_xlabel("Année")
ax.set_ylabel("Individus et lignées (Log)")
plt.show()
