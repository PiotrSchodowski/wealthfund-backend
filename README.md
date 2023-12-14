# Wealth Fund
## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [How it works](#how-it-works)
  * [Home page](#home-page)
  * [Registration and login](#registration-and-login)
  * [Profile page](#when-you-log-in-you-will-see-your-profile-page)
  * [Add wallets](#next-step-is-to-add-your-wallets)
  * [Add positions](#add-positions)
  * [Watch your positions](#watch-your-positions)
  * [Watch your portfolios in time](#watch-your-portfolios-in-time)
  * [Watch your history](#watch-your-history)
  * [Watch you profile](#watch-you-profile-with-information-about-you-fortunes-in-different-currencies)
  * [Admin page](#when-you-are-admin-you-can-manage-assets-in-special-page-)

## General info
``WealthFund`` is an application designed for easy and intuitive tracking and management of your investment portfolios. 
Whether you have multiple brokerage accounts, ledgers, or assets scattered across various exchanges, this application provides a consolidated view of all your investments in one place.

The application allows you to build portfolios that can consist of assets (over 12,000) such as:

* Stocks and ETFs from the American market (NASDAQ, NYSE, BATS, and others).
* Stocks and ETFs from the Polish market (GPW and New Connect).
* Cryptocurrencies (top 200 by global market capitalization).
* Manually added assets not found in the above categories but are held in real
accounts and need to be tracked in portfolios through the application.

Automatically fetching real-time price data for financial instruments, 
the application graphically presents the return rate over time for entire
portfolios and individual positions composed of diverse assets from around the world. 
This keeps investors informed about all their investments across different brokerage accounts,
exchanges, or ledger-type wallets (for cryptocurrencies).
All of this is based on three primary currencies: USD, EUR, and PLN.

In summary, `WealthFund` provides a comprehensive solution for investors to stay up-to-date with their diverse
investments across various brokerage accounts, exchanges, and wallets, offering a visual representation of their
financial performance in real time.

## Technologies
* Java 17
* Spring Boot 3.14
* Spring data jpa
* Spring Security JWT
* Test Containers with Docker
* Gradle
* MySQL
* Mapstruct
* Lombok
* Swagger

## Setup
To Deploy Application on your computer, you need Java 17+ and Gradle:

1. **Pull the project from the main branch** 
2. **Command Line:**
>`gradle clean build`

3. **Run .jar file with command:**
>`java -jar patch\file_name.jar`
 
4. **To run locally, you need to have MySQL installed and running on port 3306. 
   You can change the port in the application.properties file.**

5. **Pull the frontend from [Frontend](https://github.com/PiotrSchodowski/wealthfund-frontend) main branch**

6. **Entry to wealthfund-frontend and run with command:**
>`npm start`

## How it works
#### Home page
Home page is available for everyone. It contains information about the application.

![img_2.png](images/img_2.png)
### Registration and login
To use the application, you must register and log in.

![img_5.png](images/img_5.png)
![img_4.png](images/img_4.png)
### When you log in, you will see your profile page
Now you have access to all tabs, such as:
> `Wallets`
> `Positions`
> `History`

![img_6.png](images/img_6.png)
### Next step is to add your wallets 
You can do it in the **wallets** tab:

![img_8.png](images/img_8.png)

or **position** tab when it is the first your wallet:

![img_7.png](images/img_7.png)

Entry name of wallet and choose currency: 

![img_9.png](images/img_9.png)

You can add multiple wallets in different currencies:

![img_10.png](images/img_10.png)

### Add positions
Now you can add positions to your wallets.
You can do it in the **positions** tab:

![img_11.png](images/img_11.png)

First you have to choose wallet and deposit cash:

![img_12.png](images/img_12.png)
![img_13.png](images/img_13.png)

Now, you can add positions, click the **buy asset** or **+** when you would like to increase existing position

![img_15.png](images/img_15.png)
![img_14.png](images/img_14.png)

### Watch your positions:

>`on the table:`

![img_16.png](images/img_16.png)

>`on the chart:`

![img_17.png](images/img_17.png)

### Watch your portfolios in time:

>`on the table:`

![img_18.png](images/img_18.png)

>`on the chart:`

![img_19.png](images/img_19.png)

![img_20.png](images/img_20.png)

### Watch your history:

and undo when you made mistake

![img_21.png](images/img_21.png)

### Watch you profile with information about you fortunes in different currencies:

![img_23.png](images/img_23.png)

### When you are Admin, you can manage assets in special page !

![img_22.png](images/img_22.png)

Admin can import assets or create new ones if they are not in the database.

## Thank you for your attention!
If you have any questions, solutions, or ideas, please contact me at: ps.schodowski@gmail.com