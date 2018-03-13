#!/bin/sh

javadoc -sourcepath app:test -d javadoc -subpackages controllers:models:services:views:test
