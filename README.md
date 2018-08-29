# Let's Shout!

Hi LetGo, this is my coding exercise solution.

## Technologies & Architecture

I've used Akka HTTP, as that's what you guys use. Not much prior experience
with Akka. Used Spray for JSON handling and found a library to access the
Twitter API.

As for architecture - there's very little of it. I strive not to overarchitect
small projects. As most of the code is boilerplate and there's very little
business logic, there's exactly one tiny unit test.

## Problems I've run into

It took me the longest time to figure out that I can't serialize custom
exceptions. I still don't have an idea how to make the serialization work for
subclasses.

When a request comes in, pressing Enter does not stop everything. It stops the
web server, but some other stuff is running. Perhaps I'm not enough of a
perfectionist, I just let that go.

## Time spent completing the test

Yes, way too long. Akka feels tricky to debug. Probably 6-7 hours? I could've
done it a lot faster, but I also could've spent 20 hours dealing with the
issues described above. Perhaps this is a good compromise.
