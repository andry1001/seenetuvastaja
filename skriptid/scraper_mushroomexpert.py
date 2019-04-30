###########################################################################################################################
# skripti kirjutades on aluseks võetud kood järgnevatelt lehekülgedelt:                                                   #
# https://stackoverflow.com/questions/18408307/how-to-extract-and-download-all-images-from-a-website-using-beautifulsoup  #
#                                                                                                                         #
# Järgnev koodilõik korjab andmeid mushroomexpert.com andmebaasist.                                                       #
#                                                                                                                         #
###########################################################################################################################

import os
import re
import requests
from bs4 import BeautifulSoup
from selenium import webdriver

labels = []
BASEDIR = ".\ANDMED"
for SUBDIR in os.listdir(BASEDIR):
    path = os.path.join(BASEDIR, SUBDIR)
    extracted = os.path.basename(os.path.normpath(path))
    label = extracted.lower()
    labels.append(label)
BASEURL = 'https://www.mushroomexpert.com/'
driver = webdriver.PhantomJS()
for label in labels:
    URL = BASEURL + label + '.html'
    driver.get(URL)
    images = driver.find_elements_by_tag_name('img')
    i = 0
    for image in images:
        i += 1
        url = image.get_attribute('src')
        try:
            new_filename = BASEDIR + '\\' + label + '\\' + 'exp_' + str(i)+ '.jpg'
            with open(new_filename, 'wb') as f:
                if 'http' not in url:
                    url = '{}{}'.format(URL, url)
                response = requests.get(url)
                f.write(response.content)
        except:
            print(label)