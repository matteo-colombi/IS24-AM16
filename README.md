# Repository for the Software Engineering final project: group IS23-AM16

### Professor: Alessandro Margara

### Members of the group:

* Matteo Colombi [matteo.colombi@mail.polimi.it](mailto:matteo.colombo@mail.polimi.it)
* Leonardo Carlo Conti [leonardocarlo.conti@mail.polimi.it](mailto:leonardocarlo.conti@mail.polimi.it)
* Andrea Colombo [andrea78.colombo@mail.polimi.it](mailto:andrea78.colombo@mail.polimi.it)
* Lorenzo Demontis [lorenzo.demontis@mail.polimi.it](mailto:lorenzo.demontis@mail.polimi.it)

### Project goal:
Implementation in Java of Codex Naturalis, a board game published by [Cranio Creations](https://www.craniocreations.it/).

## Implemented features

| Feature        | Status |
|----------------|--------|
| Complete rules | 🟢     |
| TCP            | 🟢     |
| RMI            | 🟢     |
| CLI            | 🟢     |
| GUI            | 🟢     |
| Persistence    | 🟢     |
| Multiple games | 🟢     |
| Chat           | 🟢     |
| Resilience     | 🔴     |

### Advanced features description:
* **Chat**: Players can chat in the game, sending messages both to everyone in the game or privately to other users.
* **Persistence**: The server periodically saves the state of the game, so that it can be resumed without loosing much progress in case the connection breaks down, or the server crashes for some reason.
* **Multiple games**: The server supports multiple games being played in parallel. In our implementation, games are identified by a 6-character alphanumeric code.

## Documentation

See the [deliveries](deliveries) folder for code documentation. More specifically, we provide [JavaDoc](deliveries/javadoc) and UML [class](deliveries/uml/class_diagrams) and [sequence](deliveries/uml/sequence_diagrams) diagrams.

## How to play

To play Codex Naturalis, you must have Java version 21 or later properly installed on your system.

1. Download the appropriate `.jar` file from the [jar](deliveries/jar) folder. We provide pre-compiled jars that should work for Windows and Mac running on Arm processors. If a pre-compiled jar is not available for you, please clone the repository and compile the project yourself. 
2. Open a terminal in the folder where you have saved your jar.
3. Run the server with:
    ```bash
    java -jar ./codex-naturalis-[version].jar --server [server_ip] [socket_port] [rmi_port]
    ```
4. In another terminal, run the client:
   * Windows:
       ```bash
       java -jar .\codex-naturalis-windows.jar --client (--gui|--cli) (--socket|--rmi) (server_address):(server_port)
       ```
   * macOS running on Arm processors:
       ```bash
       java -jar ./codex-naturalis-mac-arm.jar --client (--gui|--cli) (--socket|--rmi) (server_address):(server_port)
       ```
   * Use
       ```bash
     java -jar .\codex-naturalis-[version].jar --help
       ```
     for a complete list of the options you can specify when launching the client or the server.

## Credits
The game concept and all graphical assets are property of Cranio Creations S.r.l. ([https://www.craniocreations.it/](https://www.craniocreations.it/))
