Berlin AI
=========

The 'core' code:

  berlin.action <-- represents the basic game request and response cycle as expected by the game server
  berlin.servlet <-- main class and handling of raw HTTP request parameters, JSON etc.
  berlin.game <-- processes requests and responses into a higher level representation, for use by strategies

Places to look for customizing your AI:

  berlin.setup.Setup <-- create all your strategy objects and the likes here.
  berlin.strategy <-- a nice place to put your strategies and strategy factories. There are some starter strategies in here already.