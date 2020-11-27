### Setting Enviornment

1. Install Node.js (LTS laest version) and NPM

2. Install Firebase CLI

    ```
    $ npm install -g firebase-tools
    ```

    > If you are windows, you might be going to meet security error.
    >
    > In order to fix that, you need to update policy to use Firebase CLI powershell script.
    >
    > Type below command to powershell with Admin permission.  
    >
    > $ Set-ExecutionPolicy remoteSigned

3. Login your Google account
    ```
    $ firebase login
    ```

4. Install Dependencies
    ```
    $ cd functions
    $ npm install
    ```

### Deploy

1. Use Firebase CLI to deploy
    ```
    firebase deploy
    ```