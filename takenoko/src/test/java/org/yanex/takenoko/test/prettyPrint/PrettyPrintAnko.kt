package org.yanex.takenoko.test.prettyPrint

import org.junit.Test
import org.yanex.takenoko.*
import kotlin.coroutines.experimental.CoroutineContext

class PrettyPrintAnko : AbstractPrettyPrintOutputTest() {
    override val prefix = "anko/"

    @Test fun view() = testFile {
        classDeclaration("\$\$Anko\$Factories\$AppcompatV7View", INTERNAL + OBJECT) {
            annotation<PublishedApi>()

            property("ACTION_MENU_ITEM_VIEW") {
                initializer("{ ctx: #{android.content.Context} -> #{android.support.v7.view.menu.ActionMenuItemView}(ctx)")
            }
        }

        function("actionMenuItemView", INLINE) {
            receiverType("android.view.ViewManager")
            returnType("android.support.v7.view.menu.ActionMenuItemView")

            expressionBody("actionMenuItemView {}")
        }

        function("actionMenuItemView", INLINE) {
            receiverType("android.view.ViewManager")
            param("init", KoFunctionType(
                    KoAnnotatedType(type("android.support.v7.view.menu.ActionMenuItemView")) {
                        annotation("AnkoViewDslMarker")
                    },
                    emptyList(),
                    KoType.UNIT
            ))
            returnType("android.support.v7.view.menu.ActionMenuItemView")

            body("return ankoView(`\$\$Anko\$Factories\$AppcompatV7View`.ACTION_MENU_ITEM_VIEW, theme = 0) { init() }")
        }
    }

    @Test fun rowParser() = testFile("") {
        for (i in 1..5) {
            function("rowParser") {
                (1..i).forEach { typeParam("T$it") }
                typeParam("R")

                param("parser", "(${(1..i).joinToString { "T$it" }}) -> R")
                returnType("RowParser<R>")
                
                body("""
                return object: RowParser<R> {
                    override fun parseRow(columns: Array<Any?>): R {
                        if (columns.size != $i) {
                            throw SQLiteException("Invalid row: $i ${if (i == 1) "column" else "columns"} required")
                        }

                        @Suppress("UNCHECKED_CAST")
                        return parser(${(1..i).joinToString { "columns[${it - 1}] as T$it" }})
                    }
                }
                """.trimIndent())
            }
        }
    }

    @Test fun service() = testFile {
        property("activityManager", "android.app.ActivityManager") {
            kdoc("Returns the ActivityManager instance.")

            receiverType("android.content.Context")
            getterExpression("getSystemService(Context.ACTIVITY_SERVICE) as #{android.app.ActivityManager}")
        }
    }

    @Test fun simpleListener() = testFile {
        function("onTimeChanged", INLINE) {
            receiverType("android.widget.TimePicker")
            param("l", NOINLINE, "(view: android.widget.TimePicker?, hourOfDay: Int, minute: Int) -> Unit")

            body("setOnTimeChangedListener(l)")
        }
    }

    @Test fun syntheticProperty() = testFile("test") {
        property<Int>("titleResource", VAR) {
            getterExpression {
                annotation("kotlin.Deprecated",
                        "#{test.AnkoInternals}.NO_GETTER",
                        "level = #{kotlin.DeprecationError}.ERROR")

                append("#{test.AnkoInternals}.noGetter()")
            }

            setterExpression(text = "setTitle(v)")
        }
    }

    @Test fun layouts() = testFile {
        classDeclaration("_ScrollView", OPEN) {
            primaryConstructor {
                param("ctx", "android.content.Context")
            }

            extends("android.widget.ScrollView", "ctx")

            function("lparams", INLINE) {
                typeParam("T", "android.view.View")

                receiverType("T")
                param("c", "android.content.Context?")
                param("attrs", "android.util.AttributeSet?")
                param("init", "android.widget.FrameLayout.LayoutParams.() -> Unit")

                returnType("T")

                body("""
                    val layoutParams = FrameLayout.LayoutParams(c!!, attrs!!)
                    layoutParams.init()
                    this@lparams.layoutParams = layoutParams
                    return this
                """.trimIndent())
            }
        }
    }

    @Test fun simpleCoroutineListener() = testFile {
        function("onLayoutChange") {
            param<CoroutineContext>("context")
            param("handler", KoAnnotatedType(KoFunctionType(
                    "kotlin.coroutines.experimental.CoroutineContext", listOf(
                    "v: android.view.View?",
                    "left: Int",
                    "top: Int",
                    "right: Int",
                    "bottom: Int"), "Unit")), SUSPEND)

            body("""
                addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                    launch(context) {
                        handler(v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom)
                    }
                }
            """.trimIndent())
        }
    }
}