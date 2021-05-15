from selenium import webdriver
from selenium.webdriver.common.keys import Keys
import time
import pandas as pd 

df = pd.read_csv('games_data.csv')
names = df['game']
driver = webdriver.Chrome('D:/chromedriver.exe')
driver.get('https://www.google.ca/imghp?hl=en&tab=ri&authuser=0&ogbl')


k = 0
for name in names:
    box = driver.find_element_by_xpath('//*[@id="sbtc"]/div/div[2]/input')
    box.send_keys(name + str(' cover ps4'))
    box.send_keys(Keys.ENTER)

    

    for i in range(0,1):
        try:
            driver.find_element_by_xpath('//*[@id="islrg"]/div[1]/div[1]/a[1]/div[1]/img').screenshot('C:/Users/AAYUSH/OneDrive/Desktop/labels/images/image('+str(k)+').png')
            k = k+1
        except:
            pass
    driver.get('https://www.google.ca/imghp?hl=en&tab=ri&authuser=0&ogbl')
