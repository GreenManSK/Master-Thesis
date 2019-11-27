# Vadí ztrátová komprese analýze obrazu?

## Main functionality
- Convert datasets from [Cell Tracking Challenge](http://celltrackingchallenge.net/) using multiple image codecs
- Run trackers build for this challenge on converted datasets
- Evaluate results of trackers on converted datasets with DET, TRA and SEG values used by the challenge

## How to run
Create configuration file named ```config.json``` from ```config.example.json```. Install using maven and run ```mvn exec:java -q -Dexec.args="-h""``` for help.

## Need
- Java 9
- Maven
- [ImageMagic](https://imagemagick.org/index.php)
- Codecs from [How to assess the quality of lossy compression algorithms?](https://is.muni.cz/auth/th/qnu0x/) by Martin Kulíšek