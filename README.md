# Example Mining Plugin

An example of a Kraken Plugin which utilizes RuneLite and the Kraken API to automate the simple task of mining ore at the Varrock
west mine and banking it.

This project shows how a Behavior Tree and Dependency Injection can be used to manage script state using the Kraken API.

### Example Plugin Setup

To set up your development environment we recommend following [this guide on RuneLite's Wiki](https://github.com/runelite/runelite/wiki/Building-with-IntelliJ-IDEA).

Once you have the example plugin cloned and setup within Intellij you can run the main class in `src/test/java/ExamplePluginTest.java` to run RuneLite with
the example plugin loaded in the plugin panel within RuneLite's sidebar.

In order for the plugin to run correctly your game must be in:
- Fixed mode
- Have the "Stretched Mode" plugin disabled

You should see: "Mining Plugin" within your set of plugins in the sidebar. Enable the plugin to start the script!

### Using Kraken API in Your Plugin

Add the Kraken API dependency to your `build.gradle` file as follows:

```gradle
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    // Or the latest available version
    implementation group: 'com.github.cbartram', name:'kraken-api', version: '1.0.53'
    implementation group: 'com.github.cbartram', name:'shortest-path', version: '1.0.3'
}
```
> ⚠️ If you are using the MovementService in your plugin for character pathing you should also include the `shortest-path` dependency.

## 🛠 Built With

* [Java](https://www.java.org/) — Core language
* [Gradle](https://gradle.org/) — Build tool
* [RuneLite](https://runelite.net) — Used for as the backbone for the API

---

## 🤝 Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

---

## 🔖 Versioning

We use [Semantic Versioning](http://semver.org/).
See the [tags on this repository](https://github.com/cbartram/kraken-api/tags) for available releases.

---

## 📜 License

This project is licensed under the [GNU General Public License 3.0](LICENSE.md).

---

## 🙏 Acknowledgments

* **RuneLite** — The splash screen and much of the core codebase come from RuneLite.
* **Microbot** — For clever ideas on client and plugin interaction.