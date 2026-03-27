# COMP4200 Group Project (Group 3) ---> PocketPal App (Budget App)
Group Members:
- Mustafa Ahmed
- Murtaza Ahmed
- Andrej Petrusevski
- Pramil Saravanan Rubalatha

Welcome to the COMP4200 Group 3 PocketPal App developed by Mustafa, ... (Will Update shortly)

Here in this project, our group will be utilizing JAVA as the main programming language that uses core content from our lectures.

## What is this app about?
Our finance budget app known as PocketPal was developed using Android Studio as an android application using the main source programming language Java. This app allows for users that are new to create an account with PocketPal that saves their information into our local storage. Once a user has created an account, they can set an initial balance of their own budget and manage their expenses through a simple and intuitive interface. The app does in fact use SQLite as the local database as mentioned to store any user data and expense records that will be used to retrieve and store. By adding expenses, users have the option to add, edit or delete an expense based on the user's preference on their budget savings. With each expense added, the balance becomes deducted to ensure proper calculation based on how much the user spent or saved. Another option is the wishlist that serves as a goal for users to track their savings. Users can input an item with its expected cost and users can add how much savings they have until they reach their specified goal to get that certain item. Once the user is complete with the app, a log out button is available for them to log out of the PocketPal app and prompts them to log back in with the correct credentials.

## Before getting started on this Directory
Be sure to clone this git repository on your Andriod Studio as that is a requirement. Once git cloned the repository, the app is ready to be launched. PLEASE note that you must use the MAIN BRANCH to get the best experience out of this app. If database is bugging out, please UNINSTALL the app inside the emulator and retry the app as that fixes the issue quick

## Core files for this Project
Most of the files are activities that transition one screen to another that is heavily helped by Intent. Play around with the app as a budget tracking app that helps you create an expense list, track your budget, savings for wishlist and many more!

## Files in this Repository
These are the core activity files that are used for this implementation that carries the logic to making this app work
- `AddExpenseActivity.java`
- `CreateAccountScreenActivity.java`
- `DBHelper.java`
- `Dashboard.java`
- `EditExpenseActivity.java`
- `Expense.java`
- `ExpenseAdapter.java`
- `ExpenseListActivity.java`
- `LoginScreenActivity.java`
- `MainActivity.java`
- `WishlistActivity.java`
- `WishlistItem.java`
- `README.md`
