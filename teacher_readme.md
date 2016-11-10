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

## Keyboard shortcuts

* Press b or period on your keyboard to enter the 'paused' mode. This mode is helpful when you want to take distracting slides off the screen during a presentation.
* Press s to open the presenter view (with teacher notes). This opens a new window, which is linked to the original one, so that you can navigate as usual, although you have to click on the left panel once for the keyboard shortcuts to work.
