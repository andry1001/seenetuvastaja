###########################################################################################################################
# skripti kirjutades on aluseks võetud kood järgnevatelt lehekülgedelt:                                                   #
# https://stackoverflow.com/questions/18408307/how-to-extract-and-download-all-images-from-a-website-using-beautifulsoup  #
# https://stackoverflow.com/questions/54056267/extract-images-with-selenium-python                                        #
#                                                                                                                         #                                             #
# Järgnev koodilõik korjab andmeid eElurikkus andmebaasist.                                                               #
#                                                                                                                         #
###########################################################################################################################

import os
import re
import requests
from selenium import webdriver
from selenium.common.exceptions import TimeoutException
import traceback
import time
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By

labels = []
BASEDIR = ".\ANDMED"
for SUBDIR in os.listdir(BASEDIR):
    path = os.path.join(BASEDIR, SUBDIR)
    extracted = os.path.basename(os.path.normpath(path))
    label = extracted.lower()
    labels.append(label)

BASEURL = 'https://elurikkus.ee/generic-hub/occurrences/search?taxa='
driver = webdriver.PhantomJS()
for label in labels:
    new_label = label.replace("_", '+')
    URL = BASEURL + new_label + '&sort=occurrence_date&dir=desc&pageSize=#images'
    try:
        driver.get(URL)
        images = WebDriverWait(driver, 30).until(EC.presence_of_element_located((By.TAG_NAME, 'img')))
        images = driver.find_elements_by_tag_name('img')
        i = 0
        for image in images:
            i += 1
            url = image.get_attribute('src')
            try:
                new_filename = 'elurikkus_' + label + str(i) + '.jpg'
                new_filename = BASEDIR + '\\' + label + '\\' + new_filename
                with open(new_filename, 'wb') as f:
                    if 'http' not in url:
                        url = '{}{}'.format(URL, url)
                    response = requests.get(url)
                    f.write(response.content)
            except:
                driver.save_screenshot('screenshot.png')
                print("Error 1: " + str(label))
                traceback.print_exc()
    except:
        driver.save_screenshot('screenshot.png')
        print("Error 2: " + str(label))
        traceback.print_exc()
        break