# Running slides locally

These slides are intended to be hosted online, like on github pages. While you are working on them it is helpful to run locally so that you can verify changes before committing and pushing to github.

## One-time Install

1. Install [Node.js](http://nodejs.org/) (1.0.0 or later)

1. Install dependencies
   ```sh
   npm install
   ```

1. Install Grunt.js
   ```sh
   sudo npm install -g grunt-cli
   ```

## Running the Presentation Locally

1. Serve the presentation and monitor source files for changes
   ```sh
   grunt serve
   ```

1. Open <http://127.0.0.1:8000> to view your presentation
