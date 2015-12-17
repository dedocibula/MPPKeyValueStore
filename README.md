# MPP Key-Value Store 
[![Build Status](https://travis-ci.org/dedocibula/MPPKeyValueStore.svg)](https://travis-ci.org/dedocibula/MPPKeyValueStore)

MPP is a simple in-memory key-value store written in Java 8. It provides high-level Java API/client and a lightweight socket server based on a lock-free hash map for high throughput and scalability. By default, the server features a custom Java map implementation heavily influenced by the concepts proposed in famous, heavily-cited research paper by  Maged M. Michael - 
[High Performance Dynamic Lock-Free Hash Tables and List-Based Sets](http://www.research.ibm.com/people/m/michael/spaa-2002.pdf).

## Building MPP

MPP is built using [Gradle](http://gradle.org/). To build MPP project (both server and client), run:

_Linux_
```
./gradlew assemble (build only) 
./gradlew build (build + tests)
```

_Windows_
```
gradlew.bat assemble (build only) 
gradlew.bat build (build + tests)
```

If you have Gradle distribution installed on your machine feel free to use it directly rather than relying on associated Gradle wrapper scripts.

## Running MPP Server

Running MPP server first requires [building MPP](#building-mpp). Once MPP is built, server
can be started using:

_Linux_
```
bin/mpp-server
```

_Windows_
```
bin\mpp-server.bat
```

Alternatively, you can start the server directly from your IDE. See [examples](#examples) section. 

## Examples

MPP store also comes with sample programs in the `example` directory.

<style type="text/css">
.tg  {border-collapse:collapse;border-spacing:0;}
.tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}
.tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;}
.tg .tg-yw4l{vertical-align:top}
</style>
<table class="tg">
  <tr>
    <th class="tg-yw4l">Server</th>
    <td class="tg-yw4l"><a href="https://github.com/dedocibula/MPPKeyValueStore/blob/master/src/main/java/com/javarockstars/mpp/keyvaluestore/example/ServerExample.java">com.javarockstars.mpp.keyvaluestore.example.ServerExample</a></td>
  </tr>
  <tr>
    <th class="tg-yw4l">Client</th>
    <td class="tg-yw4l"><a href="https://github.com/dedocibula/MPPKeyValueStore/blob/master/src/main/java/com/javarockstars/mpp/keyvaluestore/example/ClientExample.java">com.javarockstars.mpp.keyvaluestore.example.ClientExample</a></td>
  </tr>
</table>

## Running Tests

Much like running server testing also requires [building MPP](#building-mpp). Run tests using:

_Linux_
```
./gradlew check
```
_Windows_
```
gradlew.bat check
```
