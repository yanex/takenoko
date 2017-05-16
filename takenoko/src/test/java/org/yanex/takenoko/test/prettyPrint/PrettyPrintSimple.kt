package org.yanex.takenoko.test.prettyPrint

import org.junit.Test
import org.yanex.takenoko.KoAnnotationUseSiteTarget.*
import org.yanex.takenoko.*

class PrettyPrintSimple : AbstractPrettyPrintOutputTest() {
    override val prefix = "simple/"

    @Test fun helloWorld() = testFile {
        function("main") {
            param<Array<String>>("args")
            body("""
                println("Hello, world!")
            """.trimIndent())
        }
    }

    @Test fun fileAnnotation() = testFile {
        annotation("kotlin.Deprecated")
    }

    @Test fun userDataClass() = testFile {
        classDeclaration("User", DATA) {
            primaryConstructor {
                param<String>("firstName")
                param<String>("lastName")
                param<Int>("age")
            }
        }
    }

    @Test fun privatePrimaryConstructor() = testFile {
        classDeclaration("Test") {
            primaryConstructor(PRIVATE)
        }

        classDeclaration("Annotations") {
            primaryConstructor {
                annotation("Anno")
                annotation("Anno", true)
            }
        }
    }

    @Test fun topLevelCallables() = testFile {
        function("run", PUBLIC + INLINE) {
            typeParam("R")
            param("block", "() -> R")
            expressionBody("block()")
        }

        property<Int>("length2", INTERNAL) {
            typeParam<CharSequence>("T")
            receiverType("T")
            getterExpression("this.length")
        }

        property<String>("test", INTERNAL) {
            initializer(stringLiteral("Hello, world!"))
        }
    }

    @Test fun simpleClass() = testFile {
        classDeclaration("Test")

        classDeclaration("Another") {
            property<String>("name", CONST) {
                initializer("Jack")
            }
        }
    }

    @Test fun secondaryConstructors() = testFile {
        classDeclaration("Test") {
            typeParam<String>("T")

            primaryConstructor {
                param<String>("s")
            }

            secondaryConstructor {
                param<Int>("a")
                delegateCall("this", "a.toString()")
            }

            secondaryConstructor {
                delegateCall("this", stringLiteral("SomeString"))
            }
        }
    }

    @Test fun literals() = testFile {
        function("test") {
            body {
                appendln(stringLiteral("Simple"))
                appendln(stringLiteral("With\"Quotes"))
                appendln(charLiteral('9'))
                appendln(charLiteral('\n'))
                appendln(charLiteral('\t'))
                appendln(charLiteral('\r'))
            }
        }
    }

    @Test fun useSiteTargets() = testFile {
        classDeclaration("Test") {
            primaryConstructor {
                param<String>("a", stringLiteral("Nick")) {
                    annotation("Prop", useSiteTarget = PROPERTY)
                    annotation("Param", useSiteTarget = PARAMETER)
                    annotation("Field", useSiteTarget = FIELD)
                }
            }

            property<String>("b") {
                annotation("Getter", useSiteTarget = GETTER)
                annotation("Setter", useSiteTarget = SETTER)
                annotation("SetParam", useSiteTarget = SET_PARAMETER)
                annotation("Property", useSiteTarget = PROPERTY)
                annotation("Field", useSiteTarget = FIELD)

                initializer(stringLiteral("John"))
            }
        }
    }

    @Test fun simpleInheritance() = testFile {
        classDeclaration("A", OPEN) {
            primaryConstructor {
                param<String>("a")
            }
        }

        interfaceDeclaration("IA")
        interfaceDeclaration("IB")

        classDeclaration("B") {
            typeParam("out A")
            typeParam("in B")

            extends("A", stringLiteral("John"))
            implements("IA")
            implements("IB")
        }
    }

    @Test fun comments() = testFile {
        classDeclaration("Test") {
            kdoc("KDoc for test class.")
            comment("Comment")

            function("test") {
                kdoc("KDoc for\ntest function.")
            }

            property<String>("prop") {
                initializer(stringLiteral(""))
                comment("Multi\nline\ncomment")
            }
        }
    }
}