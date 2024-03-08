import os
import logging
import pandas as pd

df = pd.DataFrame(
    {
        "Name": ['Pierre','Paul','Marie'],
        "Age": [22, 35, 58],
    }
)

name = os.getenv("NAME", default=None)
logging.warning(f"Hello {name},  I'm Python in a container !");