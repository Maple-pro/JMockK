# JMockK

A Java adapter for MockK tool.

## To do list

Features

- [x] `mockk`
- [x] `spyk`
- [x] `spyk(Person(name()))`: use the target constructor
- [x] `mockkStatic`
- [ ] `unmockkStatic`
- [x] `mockkObject`
- [ ] `unmockkObject`
- [x] `every { }` -> `when()`
  - [x] public methods
  - [x] private methods
  - [x] static methods
  - [x] methods of object
- [x] `returns { }` -> `thenReturn()`
- [ ] mock fields
  - [ ] field in class
  - [ ] static field
  - [ ] field in object

> We need the `answers` method to support the fields mocking.

## Reference

- [MockK: Property backing fields](https://mockk.io/#property-backing-fields)
