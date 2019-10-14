Fuel Meter
===

Keep an eye on your vehicle's engine health by tracking its fuel economy. Every time you refuel make a note of how many miles you have driven from last refuel, and how much you had to put in.

You could use just pen and paper or an Excel spreadsheet or go fancy and enter the details of each refuel here. You'll get fancy-schmancy graphs and whatnot to visualise your average fuel consumption and track fuel prices and all sorts of stuff.

I personally just decided to go use this simple domain to tinker with Scala and the latest Play framework, and doobie, cats, cats-effect, oauth, etc.

## Development

To run this locally, you'll need `docker` and `docker-compose` then start your PostreSQL container like so `docker-compose up &&`
this will create postgres database with fuelmeter user/password and schema preloaded with some sample data.

I've updated my local `/etc/hosts` file to point `devpro-test` to `127.0.0.1` so that I can accept redirects from Twitter sign-in while running the app locally. Twitter does not like localhost when you're setting up the redirects in the developer console.

