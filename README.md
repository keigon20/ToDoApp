Basic To-Do App allows users to input tasks to be completed. Once the task is completed, users can mark the tast as completed.

Sample screenshots:

<img width="390" height="872" alt="Screenshot 2025-10-04 at 9 52 29 PM" src="https://github.com/user-attachments/assets/df354084-be02-431d-8e9e-a7bba43a217c" />
<img width="393" height="869" alt="Screenshot 2025-10-04 at 9 52 19 PM" src="https://github.com/user-attachments/assets/ce10e74b-39d3-4304-b954-8574ee83f3da" />

Data class:
A TodoItem data classes is used to store the task as a string and the completion status as a boolean. Once a new task is created, a new instance of this data class is created.

State:
The "items" list that stores the TodoItems is a state that is critical to the app. By having a mutable state, it allows the users to add or delete items and allows the UI to react to changes.

State hoisting:
State hoisting is used by allowing TodoScreen to control the "items" list that stores the TodoItems. It allows for functions such as onToggle and onDelete to be used. By having one controller controlling the app, 
it minimizes potential desynchronization.
