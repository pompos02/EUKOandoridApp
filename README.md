# EUKO Solutions Android App - Technical Documentation

## Overview
The EUKO Solutions Android app is a project management platform connecting clients with administrators at EUKO Solutions software development company. This application enables prospective clients to submit project requests with detailed requirements, while providing administrators with tools to view, manage, and respond to these submissions. This technical README provides an in-depth analysis of the codebase, detailing each class, its functions, relationships, and implementation details.

## Features

### Client-Side Features
- **Project Submission Form**: A user-friendly interface for potential clients to submit their project requirements
- **Simple Navigation**: Easy access to the submission form from the guest homepage
- **Detailed Project Specification**: Fields for company information, project details, technical requirements, and deadlines

### Administrator Features
- **Secure Admin Login**: Firebase Authentication for secure access to the admin dashboard
- **Project Dashboard**: View all submitted projects in a clean, organized list
- **Detailed Project View**: Access comprehensive information about each project submission
- **Project Management**: Delete completed or rejected projects
### Firebase Integration
- **Authentication**: Admin user verification
- **Firestore Database**: Real-time cloud storage for all project submissions
- **Server Timestamps**: Automatic timestamp recording for all submissions
### Activities Workflow
1. **GuestHomePageActivity**: Entry point with options to submit a form or log in as admin
2. **SubmissionFormActivity**: Collects and validates project details before sending to Firestore
3. **AdminLoginPageActivity**: Secure gateway to administrative features
4. **AdminDashboardActivity**: Displays a list of all submitted projects
5. **SubmissionDetailsActivity**: Shows complete details for a selected project submission (admin)

## Technical Architecture

### Data Model

#### `SubmissionModel.java`
A Java class that serves as the data model for project submissions with Firestore annotations.

**Key Attributes:**
- `@DocumentId private String id` - Firestore document ID, automatically mapped by Firestore
- `private String companyName` - Client company name
- `private String companyEmail` - Contact email address
- `private String companyPhone` - Contact phone number
- `private String projectDetails` - Project description and requirements
- `private String deadline` - Project deadline in "YYYY-MM-DD" format
- `private String language` - Preferred application/website language
- `private boolean newWebsite` - Indicates if client needs a new website
- `private boolean linkDatabase` - Indicates if client needs database integration
- `@ServerTimestamp private Date timestamp` - Submission timestamp automatically set by Firestore



**Firestore Annotations:**
- `@DocumentId` - Automatically maps the Firestore document ID to the `id` field
- `@ServerTimestamp` - Instructs Firestore to automatically set the timestamp upon document creation

## Application Components

### 1. Client-Side Components

#### `GuestHomePageActivity.java`
Entry point to the application, providing navigation options for clients and administrators.

**Key Methods:**
- `onCreate(Bundle savedInstanceState)` - Initializes the UI and sets up button click listeners
    - `buttonOpenForm.setOnClickListener()` - Navigates to SubmissionFormActivity
    - `buttonAdminLogin.setOnClickListener()` - Navigates to AdminLoginPageActivity

**UI Components:**
- `buttonOpenForm` - Button to access project submission form
- `buttonAdminLogin` - Button to access admin login page

#### `SubmissionFormActivity.java`
Handles the collection, validation, and submission of project request forms.

**Instance Variables:**
- UI Elements: EditText fields, CheckBoxes, Spinner, Button
- `private FirebaseFirestore db` - Firestore database instance
- `private String selectedDeadline` - Stores the date selected from DatePickerDialog

**Key Methods:**
- `onCreate(Bundle savedInstanceState)` - Sets up UI components, initializes Firebase, and configures event handlers
    - Initializes form fields and Firebase Firestore instance
    - Sets up the language spinner
    - Configures date picker for deadline selection
    - Sets up form submission handler
- `showDatePickerDialog()` - Displays date picker and formats selected date into "YYYY-MM-DD" format
    - Formats date with zero-padding for month and day values below 10
    - Updates the selectedDeadline field and displays in the EditText
- `submitRequest()` - Validates form inputs and submits data to Firestore
    - Retrieves and validates all form inputs
    - Creates a data map with all submission fields
    - Adds current date as timestamp
    - Uses Firestore's `add()` method to create a new document in "submissions" collection


**Form Validation Logic:**
- Company name: Must not be empty
- Email: Must not be empty and must match email pattern
- Phone: Must not be empty and length must be at least 6 characters
- Project details: Must not be empty

### 2. Admin-Side Components

#### `AdminLoginPageActivity.java`
Provides authentication for administrative access using Firebase Authentication.

**Instance Variables:**
- `private EditText editTextEmail, editTextPassword` - Input fields for credentials
- `private Button buttonLogin` - Button to trigger authentication
- `private FirebaseAuth auth` - Firebase Authentication instance

**Key Methods:**
- `onCreate(Bundle savedInstanceState)` - Initializes the UI and sets up Firebase Authentication
- `loginAdmin(String email, String password)` - Authenticates admin credentials
    - Validates that email and password are not empty
    - Uses `signInWithEmailAndPassword()` from FirebaseAuth
    - On success: Navigates to AdminDashboardActivity and finishes current activity
    - On failure: Displays error message Toast with exception details

#### `AdminDashboardActivity.java`
Displays a list of all project submissions using a RecyclerView.

**Instance Variables:**
- `private RecyclerView recyclerViewSubmissions` - UI component to display submission list
- `private SubmissionAdapter submissionAdapter` - Custom adapter for the RecyclerView
- `private FirebaseFirestore db` - Firestore database instance

**Key Methods:**
- `onCreate(Bundle savedInstanceState)` - Initializes components and starts data loading
    - Sets up RecyclerView with LinearLayoutManager
    - Initializes Firebase Firestore instance
    - Creates adapter
    - Calls `loadSubmissions()` to fetch data
- `loadSubmissions()` - Fetches submissions from Firestore and updates the UI
    - Queries "submissions" collection ordered by timestamp (descending)
    - Converts each DocumentSnapshot (Firestore) to SubmissionModel object
    - Sets document ID on each submission model
    - Adds models to a list and updates the adapter

**Firestore Query:**
- Collection: "submissions"
- Order: By "timestamp" field in descending order (newest first)
- Conversion: DocumentSnapshot to SubmissionModel objects

#### `SubmissionDetailsActivity.java`
Displays detailed information for a specific submission and provides deletion capability.

**Instance Variables:**
- UI Elements: Multiple TextView fields for displaying submission data
- `private Button buttonDelete` - Button to delete the submission
- `private FirebaseFirestore db` - Firestore database instance
- `private String documentId` - ID of the current submission document
- `private SimpleDateFormat timestampFormat` - Formatter for displaying timestamp in readable format

**Key Methods:**
- `onCreate(Bundle savedInstanceState)` - Initializes UI components and loads data
    - Initializes all TextViews and the delete button
    - Retrieves document ID from Intent extras
    - Validates document ID and loads submission data
    - Sets up delete button click listener
- `loadSubmissionDetail()` - Fetches and displays submission details
    - Queries Firestore for specific document by ID
    - Converts document to SubmissionModel
    - Populates all TextViews with submission data
    - Formats timestamp with defined SimpleDateFormat

- `deleteSubmission()` - Deletes the current submission from Firestore
    - Validates document ID
    - Uses Firestore `delete()` method

**Error Handling:**
- Uses Log class for detailed error logging
- Catches and displays specific FirebaseFirestoreException details
- Validates document ID before operations
- Handles null checks for all submission fields

### 3. Adapter Components

#### `SubmissionAdapter.java`
Custom RecyclerView adapter that binds submission data to list item views.

**Instance Variables:**
- `private List<SubmissionModel> submissionList` - Data source for the adapter
- `private AdminDashboardActivity context` - Activity context reference

**Key Methods:**
- Constructor `SubmissionAdapter(List<SubmissionModel>, AdminDashboardActivity)` - Initializes adapter with data and context
- `onCreateViewHolder(ViewGroup parent, int viewType)` - Creates the layout for list items
    - Creates R.layout.submission_item layout
    - Returns new SubmissionViewHolder instance
- `onBindViewHolder(SubmissionViewHolder holder, int position)` - Binds data to ViewHolder
    - Sets company name and deadline text
    - Configures item click listener to open SubmissionDetailsActivity
    - Passes document ID via Intent extras
- `getItemCount()` - Returns the number of items in the list
- `setSubmissions(List<SubmissionModel>)` - Updates adapter data and refreshes UI
    - Clears existing list
    - Adds all new submissions
    - Calls notifyDataSetChanged() to refresh RecyclerView

**ViewHolder Class:**
- Static nested class `SubmissionViewHolder` extends RecyclerView.ViewHolder
- Holds references to TextViews in the list item layout
- Constructor finds views by ID to avoid repeated calls to findViewById in onBindViewHolder

## Firebase Implementation Details

### Firestore Database Structure
- **Collection:** "submissions"
- **Documents:** Each document represents one project submission
- **Fields:** Match the attributes in SubmissionModel

### Authentication
- Email/password authentication for admin access
- No registration functionality included (assumed pre-registered admin accounts)

### Database Operations
1. **Create:** In SubmissionFormActivity using `add()` method
2. **Read (List):** In AdminDashboardActivity using collection query
3. **Read (Single):** In SubmissionDetailsActivity using document reference
4. **Delete:** In SubmissionDetailsActivity using document reference's `delete()` method
5. **Update:** Not implemented in the current codebase

## Form Validation & Error Handling

### Input Validation
- Company name: Non-empty check
- Email: Non-empty check and pattern matching using `Patterns.EMAIL_ADDRESS`
- Phone: Non-empty check and minimum length validation (6 characters)
- Project details: Non-empty check

### Error Handling Patterns
- Toast messages for user feedback
- Log messages for debugging (using TAG constants)
- Specific exception catching for Firebase operations
- Document existence checking before operations
- Null safety checks throughout the codebase

## UI Components and Layouts

### Referenced Layouts:
1. `activity_guest_home.xml` - Home screen with navigation buttons
2. `activity_submission_form.xml` - Client project submission form
3. `activity_admin_login_page.xml` - Admin authentication screen
4. `activity_admin_dashboard.xml` - List of submissions
5. `activity_submission_details.xml` - Detailed view of a submission
6. `submission_item.xml` - Individual list item in RecyclerView

### Data Display Strategy:
- List view shows minimal information (company name, deadline)
- Details view shows all submission information
- Null values handled with default text ("N/A" or similar)
- Boolean values converted to string representation

## Lifecycle Management

The application follows standard Android activity lifecycle patterns:
- `onCreate()` is used for initialization
- `finish()` is called after successful operations to return to previous screens




## Security Implementation

### Authentication Security:
- Firebase Authentication for admin access
- No stored credentials in code

### Data Access Security:
- No client-side permission checks (relies on Firebase security rules)
- Document ID validation before operations
- Input validation to prevent malformed data

## Installation & Setup Requirements

### Required Dependencies:
```gradle
[versions]
agp = "8.9.1"
firebaseBom = "33.12.0"
junit = "4.13.2"
junitVersion = "1.1.5"
espressoCore = "3.5.1"
appcompat = "1.6.1"
material = "1.10.0"
firebaseAuth = "23.2.0"
credentials = "1.5.0"
credentialsPlayServicesAuth = "1.5.0"
googleid = "1.1.1"
firebaseFirestore = "25.1.3"

[libraries]
firebase-bom = { module = "com.google.firebase:firebase-bom", version.ref = "firebaseBom" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
ext-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
material = { group = "com.google.android.material", name = "material", version.ref = "material" }
firebase-auth = { group = "com.google.firebase", name = "firebase-auth", version.ref = "firebaseAuth" }
credentials = { group = "androidx.credentials", name = "credentials", version.ref = "credentials" }
credentials-play-services-auth = { group = "androidx.credentials", name = "credentials-play-services-auth", version.ref = "credentialsPlayServicesAuth" }
googleid = { group = "com.google.android.libraries.identity.googleid", name = "googleid", version.ref = "googleid" }
firebase-firestore = { group = "com.google.firebase", name = "firebase-firestore", version.ref = "firebaseFirestore" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }


```
```gradle
dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.auth)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
```

### Firebase Setup:
1. Create Firebase project
2. Add Android app to Firebase project
3. Download google-services.json to app directory
4. Enable Authentication with Email/Password provider
5. Create Firestore database
6. Set appropriate security rules

## Conclusion

The EUKO Solutions Android app demonstrates a well-structured implementation of a client-admin project management system using Firebase services. The code follows standard Android development patterns with clear separation of responsibilities across activities and components. The app employs proper form validation, error handling, and Firebase integration techniques, making it a solid foundation for further development and enhancement.
