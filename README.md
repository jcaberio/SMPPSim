SMPPSim
=======

SMPPSim is a SMPP SMSC simulation tool, designed to help you test your SMPP based application.

SMPPSim is free of charge and open source.

Build
-----
```
$ ant compile jar
```

Run
---
```
$ sh ./startsmpp.sh
```

Config
------
```
$ vim conf/smppsim.props
```

**Simulate failure scenario using throttling**
```
ESME_RTHROTTLED=true
```
**Life cycle manager for load testing, sets message state according to the MT message**
```
LIFE_CYCLE_MANAGER=com.seleniumsoftware.SMPPSim.LoadTestLifeCycleManager
```