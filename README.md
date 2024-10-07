# About the project

I developed this project to create an Android app that allows users to browse different fast food options and add them to a cart. The app offers four main categories: pizzas, burgers, tacos, and salads, so users can easily find what they’re craving.

While I haven’t added the ability to make purchases yet, I’ve built a solid system for navigating and managing items. I focused on keeping the design simple and easy to use, so anyone can navigate the app without any hassle.
My goal with this project was to practice and apply what I have learned about android development, this as a starting point, and more updates will be added in the future like like adding payment options.

Bellow is an overview of the functionalities offered by the application.

### 1. Browsing and adding items to cart

The application features four product categories: **Pizza, Burger, Tacos, and Salad**. Within each category, a selection of food items is displayed, each with basic information such as ingredients, price, and rating to help users quickly identify their choices. Users can select any item for more information or to add it to the cart.

Additionally, the application offers a  search functionality to help the user look for a specific item, however,the search is limited to the currently selected category.

![picture 1](https://github.com/user-attachments/assets/151c77f0-e5d5-44b4-9a44-620ec6a0fb60)


### 2. User Authentication and Account Setup
If a user is using the app for the first time, adding an item to the cart or navigating to the cart will redirect them to the login page, where they can enter their credentials. New users can create an account by signing up. Both the login and sign-up forms include input validation to prevent empty submissions. Upon form submission, a query checks the database for the user's credentials. If the credentials match an entry, a success message is displayed for login attempts. For sign-ups, an error message appears if the account already exists, preventing duplicate accounts.

![picture 2](https://github.com/user-attachments/assets/216651fd-bf0b-4bb0-a040-82ed9c1b59e4)
