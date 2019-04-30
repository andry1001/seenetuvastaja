###########################################################################################################################
# skripti kirjutades on aluseks võetud kood järgnevatelt lehekülgedelt:                                                   #
# https://stackoverflow.com/questions/18408307/how-to-extract-and-download-all-images-from-a-website-using-beautifulsoup  #
#                                                                                                                         #
# Järgnev koodilõik korjab andmeid mushroom.world andmebaasist.                                                           #
#                                                                                                                         #
###########################################################################################################################

import os
import re
import requests
from bs4 import BeautifulSoup

labels = []
BASEDIR = ".\ANDMED"
for SUBDIR in os.listdir(BASEDIR):
    path = os.path.join(BASEDIR, SUBDIR)
    extracted = os.path.basename(os.path.normpath(path))
    label = extracted.lower()
    labels.append(label)

BASEURL = 'http://www.mushroom.world/show?n='
for label in labels:
    new_label = label.replace("_", "-")
    URL = BASEURL + new_label
    response = requests.get(URL)
    soup = BeautifulSoup(response.text, 'html.parser')
    img_tags = soup.find_all('img')
    urls = [img['src'] for img in img_tags]
    for url in urls:
        try:
            mod_url = "http://www.mushroom.world" + url.split("..")[1]
            filename = re.search(r'/([\w_-]+[.](JPG|GIF|PNG))$', mod_url)
            new_filename = filename.group(1)
            new_filename = BASEDIR + '\\' + label + '\\' + new_filename
            with open(new_filename, 'wb') as f:
                if 'http' not in mod_url:
                    url = '{}{}'.format(URL, mod_url)
                response = requests.get(mod_url)
                f.write(response.content)
            f.close()
        except:
            print(label)