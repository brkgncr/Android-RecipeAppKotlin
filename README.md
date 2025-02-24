# Recipe App - Android Application

RecipeApp is a simple Android app that allows users to easily add, view, and delete recipes. The app is developed using **Kotlin** and follows the **MVVM (Model-View-ViewModel)** architecture for a cleaner and more maintainable code structure.

## Features
- **Recipe Listing:** Starts with an empty list, and as recipes are added, they appear in the list.
- **Add Recipe:** Users can click the + button to add a recipe by providing an image, name, and ingredients.
- **View and Edit Recipe:** Tapping on a recipe in the list shows its details, and users can view or edit it.
- **Delete Recipe:** Recipes can be deleted from the details screen using the Delete button.

## Technologies Used
- **Kotlin:** The app is developed using Kotlin.
- **Room Database:** Data is stored locally using Room.
- **Navigation Component:** Navigation between screens is handled using the Navigation Component.
- **RxJava3:** Reactive programming is implemented with RxJava3 and RxAndroid.
- **ViewBinding:** ViewBinding is enabled for easy access to UI components.
- **MVVM Architecture:** The app uses the **MVVM** (Model-View-ViewModel) architecture. This means the app's **Model** manages the data, the **ViewModel** contains the business logic and provides data to the UI, and the **View** displays the UI. This separation makes the code easier to manage, test, and maintain.

## Setup Instructions

### Clone the Repository:
```bash
git clone https://github.com/brkgncr/Android-RecipeAppKotlin.git
