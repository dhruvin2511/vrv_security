# Role-Based Access Control Android Application

This project is an Android application that implements **Authentication**, **Authorization**, and **Role-Based Access Control (RBAC)**. The app allows users to register and log in with one of two roles: **Farmer** or **Customer**. Based on their roles, users are redirected to specific dashboards with access to role-specific features and data.

---

## Features

1. **User Authentication**  
   - Secure login and signup using Firebase Authentication.
   - Password validation for account creation.

2. **Role-Based Access Control (RBAC)**  
   - Two roles supported: 
     - **Farmer**: Redirected to `MainActivity` after login.
     - **Customer**: Redirected to `CustomerActivity` after login.
   - Each role has specific features and data access permissions.

3. **Authorization**  
   - Users can only access data and features relevant to their assigned role.

4. **Firebase Integration**  
   - Firebase Realtime Database is used to store user profiles, including roles and other data.

---
## Technologies Used

- **Kotlin**: Programming language for Android development.
- **Firebase**:
  - Firebase Authentication for secure login and signup.
  - Firebase Realtime Database for storing user roles and data.
- **Android Jetpack Components**:
  - ViewBinding for managing UI.
  - Activity Lifecycle for efficient navigation.

---

## Screenshots

![Screenshot 2024-11-29 135559](https://github.com/user-attachments/assets/22d47d5d-1e61-405c-b2c3-11c2b82bdbdb)

---

## Demo



### Video
Check out the full app demo:

https://github.com/user-attachments/assets/7b8c3941-a0a6-47d3-bd16-3d3bd32b8ef3

---

## How It Works

1. **Signup Process**:
   - Users register by providing their name, email, password, and selecting a role (`Farmer` or `Customer`).
   - User data is stored in Firebase Realtime Database with the following structure:
     ```json
     {
       "user": {
         "userId1": {
           "name": "John Doe",
           "email": "john@example.com",
           "role": "Farmer"
         },
         "userId2": {
           "name": "Jane Smith",
           "email": "jane@example.com"
           "role": "Customer"
         }
       }
     }
     
2. **Login Process**:
   - Users log in with their email and password.
   - Authentication validates the credentials using Firebase.
   - The app fetches the user's role from the database and navigates them to the appropriate dashboard.

3. **Role-Specific Access**:
   - **Farmer**: Access `MainActivity` for managing crops, tasks, and farm data.
   - **Customer**: Access `CustomerActivity` for browsing products, placing orders, and viewing order history.

---

## Setup and Installation

### Prerequisites
1. Android Studio installed on your system.
2. A Firebase project set up with:
   - Firebase Authentication.
   - Firebase Realtime Database.

### Steps to Run the Project
1. Clone the repository:
   ```bash
   git clone <repository-url>
2. Open the project in Android Studio.
3. Connect the app to your Firebase project:
   - Add the google-services.json file to the app/ directory.
   - Enable Firebase Authentication and Realtime Database in the Firebase Console.
4. Update the Realtime Database rules for read/write permissions:
   ```json
   {
    "rules": {
      "user": {
        ".read": "auth != null",
        ".write": "auth != null"
      }
    }
  }
5. Build and run the app on an emulator or physical device.

---

