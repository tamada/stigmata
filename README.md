This project has no longer maintenanced.
Please use [`tamada/pochi`](https://github.com/tamada/pochi) instead of this project.

# Stigmata: Java birthmark toolkit

[![Build Status](https://travis-ci.org/tamada/stigmata.svg?branch=master)](https://travis-ci.org/tamada/stigmata) [![Coverage Status](https://coveralls.io/repos/tamada/stigmata/badge.svg)](https://coveralls.io/r/tamada/stigmata)

## Overview

Stigmata is the Java birthmark toolkit, which aims to detect the theft
of programs.  This tool can extract birthmarks from Java class files
directory, and compare them.

A birthmark is a set of special information that the program
originally possesses.  The birthmark is carefully extracted from
critical portions of class file.  Hence, if a class file P has the
same birthmark as another class file Q's, Q is very likely to be a
copy of P.  Thus, the birthmark can be used as a simple but powerful
signature to distinguish doubtful class files (those which seem to be
copies).

## Features

Stigmata support the extracting birthmarks from Java class files, and
written in Java SE 7 with ASM.

The main features are:

* extraction of the four types of birthmarks directly from Java
    class files (without source code),
* pairwise birthmark comparison of Java class files,
* Jar file and War file support,
* plug-in architecture for new birthmarks, and

## Requirements

Stigmata requires following libraries.

* Requirements for Runtime
    * ASM 5.0.2 (http://asm.objectweb.org/)
    * Apache Commons DBUtils 1.1 (http://commons.apache.org/dbutils/)
    * Apache Commons Beanutils 1.7.0 (http://commons.apache.org/beanutils/)
    * Talisman XmlCli 1.2.2 (http://talisman.sourceforge.jp/xmlcli/)
    * Talisman i18n 1.0.1 (http://talisman.sourceforge.jp/i18n/)
* Requirement for Testing
    * JUnit 4.12 (http://www.junit.org/)

## Author

* **Name**:   Haruaki TAMADA.
* **Affiliation**: Faculty of Computer Science and Engineering, Kyoto Sangyo University.
* **E-mail**:      tamada@users.noreply.github.com
* **Web Page**:    http://github.com/tamada/stigmata/

Please notify us some bugs and requests on https://github.com/tamada/stigmata.
