package dev.romainguy.apex

import kotlin.reflect.KClass

open class Element(content: Element.() -> Unit = { }) {
    var parent = this
        private set

    private val children = mutableListOf<Element>()
    private val components = mutableListOf<Any>()

    init {
        content()
    }

    fun addChild(child: Element) {
        child.parent = this
        children.add(child)
    }

    fun child(action: (Element) -> Unit) {
        for (child in children) {
            action(child)
        }
    }

    fun addComponent(component: Any) {
        components.add(component)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> component(type: KClass<T>) = components.first { type.isInstance(it) } as T

    inline fun <reified T : Any> component() = component(T::class)

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> componentOrNull(type: KClass<T>) =
        components.firstOrNull { type.isInstance(it) } as T?

    inline fun <reified T : Any> componentOrNull() = componentOrNull(T::class)

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> component(type: KClass<T>, action: T.()-> Unit) {
        for (component in components) {
            if (type.isInstance(component)) (component as T).action()
        }
    }

    inline fun <reified T : Any> component(noinline action: T.() -> Unit) {
        component(T::class, action)
    }

    fun ChildElement(content: Element.() -> Unit): Element {
        val child = Element()
        child.content()
        addChild(child)
        return child
    }
}
