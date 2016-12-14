About
=

Box2DCrushingExample is a simple [libGDX](https://github.com/libgdx/libgdx) project illustrating a trick that allows simulation of a crushing detection with Box2D.

Why
-

With the help of libGDX and Box2D, one can easily detect a collision between two bodies using a `ContactListener`. However, this doesn't offers a direct, built-in method to detect when a body is being crushed between other bodies.

This could be usefull for platform games and the likes that feature moving objets possibly crushing characters, etc.

How it works
-

The trick here is to place a hidden body "inside" the apparently ***crusher*** body and make use of the `UserData` to tag the smaller body as ***destroyer***.

So when a dynamic body is crushed between two other bodies, it quickly overlaps al least one of them, thus allowing contact with the hidden ***destroyer*** body.

![](https://molinan2.github.io/box2d-crushing-example/resource/box2d-crushing.png)

In Box2D (at least in libGDX 1.9.5) when a dynamic body is left with no space to move, it ovelarps its colliding bodies. This requires the colliding bodies to be kinetic or static, so no force is applied to them by the dynamic body.

How to compile
-

Just import the project using Android Studio and let Gradle configure the dependencies. There are more details in the libGDX wiki for [IntelliJ IDEA](https://github.com/libgdx/libgdx/wiki/Gradle-and-Intellij-IDEA), [Eclipse](https://github.com/libgdx/libgdx/wiki/Gradle-and-Eclipse), [Netbeans](https://github.com/libgdx/libgdx/wiki/Gradle-and-NetBeans) and [Command line](https://github.com/libgdx/libgdx/wiki/Gradle-on-the-Commandline).

I've tested the Desktop and HTML modules. I didn't test the Android module but it should work.

![](https://molinan2.github.io/box2d-crushing-example/resource/box2d-crushing-desktop.png)