package io.eberlein.oui

abstract class Storage<T>(val savePath: String, var isOpen: Boolean=false) : IStorage<T>
abstract class OUIStorage(savePath: String) : Storage<OUIEntry>(savePath), IOUIEntryStorage
