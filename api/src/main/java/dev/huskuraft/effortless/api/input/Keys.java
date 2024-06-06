package dev.huskuraft.effortless.api.input;

public enum Keys implements Key {
    KEY_UNKNOWN("key.keyboard.unknown", KeyCodes.KEY_UNKNOWN),

    MOUSE_BUTTON_LEFT("key.mouse.left", KeyCodes.MOUSE_BUTTON_LEFT),
    MOUSE_BUTTON_RIGHT("key.mouse.right", KeyCodes.MOUSE_BUTTON_LEFT),
    MOUSE_BUTTON_MIDDLE("key.mouse.middle", KeyCodes.MOUSE_BUTTON_LEFT),
    MOUSE_BUTTON_3("key.mouse.4", KeyCodes.MOUSE_BUTTON_3),
    MOUSE_BUTTON_4("key.mouse.5", KeyCodes.MOUSE_BUTTON_4),
    MOUSE_BUTTON_5("key.mouse.6", KeyCodes.MOUSE_BUTTON_5),
    MOUSE_BUTTON_6("key.mouse.7", KeyCodes.MOUSE_BUTTON_6),
    MOUSE_BUTTON_7("key.mouse.8", KeyCodes.MOUSE_BUTTON_7),

    KEY_0("key.keyboard.0", KeyCodes.KEY_0),
    KEY_1("key.keyboard.1", KeyCodes.KEY_1),
    KEY_2("key.keyboard.2", KeyCodes.KEY_2),
    KEY_3("key.keyboard.3", KeyCodes.KEY_3),
    KEY_4("key.keyboard.4", KeyCodes.KEY_4),
    KEY_5("key.keyboard.5", KeyCodes.KEY_5),
    KEY_6("key.keyboard.6", KeyCodes.KEY_6),
    KEY_7("key.keyboard.7", KeyCodes.KEY_7),
    KEY_8("key.keyboard.8", KeyCodes.KEY_8),
    KEY_9("key.keyboard.9", KeyCodes.KEY_9),
    KEY_A("key.keyboard.a", KeyCodes.KEY_A),
    KEY_B("key.keyboard.b", KeyCodes.KEY_B),
    KEY_C("key.keyboard.c", KeyCodes.KEY_C),
    KEY_D("key.keyboard.d", KeyCodes.KEY_D),
    KEY_E("key.keyboard.e", KeyCodes.KEY_E),
    KEY_F("key.keyboard.f", KeyCodes.KEY_F),
    KEY_G("key.keyboard.g", KeyCodes.KEY_G),
    KEY_H("key.keyboard.h", KeyCodes.KEY_H),
    KEY_I("key.keyboard.i", KeyCodes.KEY_I),
    KEY_J("key.keyboard.j", KeyCodes.KEY_J),
    KEY_K("key.keyboard.k", KeyCodes.KEY_K),
    KEY_L("key.keyboard.l", KeyCodes.KEY_L),
    KEY_M("key.keyboard.m", KeyCodes.KEY_M),
    KEY_N("key.keyboard.n", KeyCodes.KEY_N),
    KEY_O("key.keyboard.o", KeyCodes.KEY_O),
    KEY_P("key.keyboard.p", KeyCodes.KEY_P),
    KEY_Q("key.keyboard.q", KeyCodes.KEY_Q),
    KEY_R("key.keyboard.r", KeyCodes.KEY_R),
    KEY_S("key.keyboard.s", KeyCodes.KEY_S),
    KEY_T("key.keyboard.t", KeyCodes.KEY_T),
    KEY_U("key.keyboard.u", KeyCodes.KEY_U),
    KEY_V("key.keyboard.v", KeyCodes.KEY_V),
    KEY_W("key.keyboard.w", KeyCodes.KEY_W),
    KEY_X("key.keyboard.x", KeyCodes.KEY_X),
    KEY_Y("key.keyboard.y", KeyCodes.KEY_Y),
    KEY_Z("key.keyboard.z", KeyCodes.KEY_Z),
    KEY_F1("key.keyboard.f1", KeyCodes.KEY_F1),
    KEY_F2("key.keyboard.f2", KeyCodes.KEY_F2),
    KEY_F3("key.keyboard.f3", KeyCodes.KEY_F3),
    KEY_F4("key.keyboard.f4", KeyCodes.KEY_F4),
    KEY_F5("key.keyboard.f5", KeyCodes.KEY_F5),
    KEY_F6("key.keyboard.f6", KeyCodes.KEY_F6),
    KEY_F7("key.keyboard.f7", KeyCodes.KEY_F7),
    KEY_F8("key.keyboard.f8", KeyCodes.KEY_F8),
    KEY_F9("key.keyboard.f9", KeyCodes.KEY_F9),
    KEY_F10("key.keyboard.f10", KeyCodes.KEY_F10),
    KEY_F11("key.keyboard.f11", KeyCodes.KEY_F11),
    KEY_F12("key.keyboard.f12", KeyCodes.KEY_F12),
    KEY_F13("key.keyboard.f13", KeyCodes.KEY_F13),
    KEY_F14("key.keyboard.f14", KeyCodes.KEY_F14),
    KEY_F15("key.keyboard.f15", KeyCodes.KEY_F15),
    KEY_F16("key.keyboard.f16", KeyCodes.KEY_F16),
    KEY_F17("key.keyboard.f17", KeyCodes.KEY_F17),
    KEY_F18("key.keyboard.f18", KeyCodes.KEY_F18),
    KEY_F19("key.keyboard.f19", KeyCodes.KEY_F19),
    KEY_F20("key.keyboard.f20", KeyCodes.KEY_F20),
    KEY_F21("key.keyboard.f21", KeyCodes.KEY_F21),
    KEY_F22("key.keyboard.f22", KeyCodes.KEY_F22),
    KEY_F23("key.keyboard.f23", KeyCodes.KEY_F23),
    KEY_F24("key.keyboard.f24", KeyCodes.KEY_F24),
    KEY_F25("key.keyboard.f25", KeyCodes.KEY_F25),
    KEY_NUM_LOCK("key.keyboard.num.lock", KeyCodes.KEY_NUM_LOCK),
    KEY_KEYPAD_0("key.keyboard.keypad.0", KeyCodes.KEY_KP_0),
    KEY_KEYPAD_1("key.keyboard.keypad.1", KeyCodes.KEY_KP_1),
    KEY_KEYPAD_2("key.keyboard.keypad.2", KeyCodes.KEY_KP_2),
    KEY_KEYPAD_3("key.keyboard.keypad.3", KeyCodes.KEY_KP_3),
    KEY_KEYPAD_4("key.keyboard.keypad.4", KeyCodes.KEY_KP_4),
    KEY_KEYPAD_5("key.keyboard.keypad.5", KeyCodes.KEY_KP_5),
    KEY_KEYPAD_6("key.keyboard.keypad.6", KeyCodes.KEY_KP_6),
    KEY_KEYPAD_7("key.keyboard.keypad.7", KeyCodes.KEY_KP_7),
    KEY_KEYPAD_8("key.keyboard.keypad.8", KeyCodes.KEY_KP_8),
    KEY_KEYPAD_9("key.keyboard.keypad.9", KeyCodes.KEY_KP_9),
    KEY_KEYPAD_ADD("key.keyboard.keypad.add", KeyCodes.KEY_KP_ADD),
    KEY_KEYPAD_DECIMAL("key.keyboard.keypad.decimal", KeyCodes.KEY_KP_DECIMAL),
    KEY_KEYPAD_ENTER("key.keyboard.keypad.enter", KeyCodes.KEY_KP_ENTER),
    KEY_KEYPAD_EQUAL("key.keyboard.keypad.equal", KeyCodes.KEY_KP_EQUAL),
    KEY_KEYPAD_MULTIPLY("key.keyboard.keypad.multiply", KeyCodes.KEY_KP_MULTIPLY),
    KEY_KEYPAD_DIVIDE("key.keyboard.keypad.divide", KeyCodes.KEY_KP_DIVIDE),
    KEY_KEYPAD_SUBTRACT("key.keyboard.keypad.subtract", KeyCodes.KEY_KP_SUBTRACT),
    KEY_DOWN("key.keyboard.down", KeyCodes.KEY_DOWN),
    KEY_LEFT("key.keyboard.left", KeyCodes.KEY_LEFT),
    KEY_RIGHT("key.keyboard.right", KeyCodes.KEY_RIGHT),
    KEY_UP("key.keyboard.up", KeyCodes.KEY_UP),
    KEY_APOSTROPHE("key.keyboard.apostrophe", KeyCodes.KEY_APOSTROPHE),
    KEY_BACKSLASH("key.keyboard.backslash", KeyCodes.KEY_BACKSLASH),
    KEY_COMMA("key.keyboard.comma", KeyCodes.KEY_COMMA),
    KEY_EQUAL("key.keyboard.equal", KeyCodes.KEY_EQUAL),
    KEY_GRAVE_ACCENT("key.keyboard.grave.accent", KeyCodes.KEY_GRAVE_ACCENT),
    KEY_LEFT_BRACKET("key.keyboard.left.bracket", KeyCodes.KEY_LEFT_BRACKET),
    KEY_MINUS("key.keyboard.minus", KeyCodes.KEY_MINUS),
    KEY_PERIOD("key.keyboard.period", KeyCodes.KEY_PERIOD),
    KEY_RIGHT_BRACKET("key.keyboard.right.bracket", KeyCodes.KEY_RIGHT_BRACKET),
    KEY_SEMICOLON("key.keyboard.semicolon", KeyCodes.KEY_SEMICOLON),
    KEY_SLASH("key.keyboard.slash", KeyCodes.KEY_SLASH),
    KEY_SPACE("key.keyboard.space", KeyCodes.KEY_SPACE),
    KEY_TAB("key.keyboard.tab", KeyCodes.KEY_TAB),
    KEY_LEFT_ALT("key.keyboard.left.alt", KeyCodes.KEY_LEFT_ALT),
    KEY_LEFT_CONTROL("key.keyboard.left.control", KeyCodes.KEY_LEFT_CONTROL),
    KEY_LEFT_SHIFT("key.keyboard.left.shift", KeyCodes.KEY_LEFT_SHIFT),
    KEY_LEFT_WIN("key.keyboard.left.win", KeyCodes.KEY_LEFT_SUPER),
    KEY_RIGHT_ALT("key.keyboard.right.alt", KeyCodes.KEY_RIGHT_ALT),
    KEY_RIGHT_CONTROL("key.keyboard.right.control", KeyCodes.KEY_RIGHT_CONTROL),
    KEY_RIGHT_SHIFT("key.keyboard.right.shift", KeyCodes.KEY_RIGHT_SHIFT),
    KEY_RIGHT_WIN("key.keyboard.right.win", KeyCodes.KEY_RIGHT_SUPER),
    KEY_ENTER("key.keyboard.enter", KeyCodes.KEY_ENTER),
    KEY_ESCAPE("key.keyboard.escape", KeyCodes.KEY_ESCAPE),
    KEY_BACKSPACE("key.keyboard.backspace", KeyCodes.KEY_BACKSPACE),
    KEY_DELETE("key.keyboard.delete", KeyCodes.KEY_DELETE),
    KEY_END("key.keyboard.end", KeyCodes.KEY_END),
    KEY_HOME("key.keyboard.home", KeyCodes.KEY_HOME),
    KEY_INSERT("key.keyboard.insert", KeyCodes.KEY_INSERT),
    KEY_PAGE_DOWN("key.keyboard.page.down", KeyCodes.KEY_PAGE_DOWN),
    KEY_PAGE_UP("key.keyboard.page.up", KeyCodes.KEY_PAGE_UP),
    KEY_CAPS_LOCK("key.keyboard.caps.lock", KeyCodes.KEY_CAPS_LOCK),
    KEY_PAUSE("key.keyboard.pause", KeyCodes.KEY_PAUSE),
    KEY_SCROLL_LOCK("key.keyboard.scroll.lock", KeyCodes.KEY_SCROLL_LOCK),
    KEY_MENU("key.keyboard.menu", KeyCodes.KEY_MENU),
    KEY_PRINT_SCREEN("key.keyboard.print.screen", KeyCodes.KEY_PRINT_SCREEN),
    KEY_WORLD_1("key.keyboard.world.1", KeyCodes.KEY_WORLD_1),
    KEY_WORLD_2("key.keyboard.world.2", KeyCodes.KEY_WORLD_2),
    ;

    private final KeyBinding keyBinding;

    Keys(String nameKey, KeyCodes key) {
        this.keyBinding = new KeyBinding() {
            @Override
            public String getName() {
                return nameKey;
            }

            @Override
            public String getCategory() {
                return null;
            }

            @Override
            public int getDefaultKey() {
                return key.value();
            }

            @Override
            public boolean consumeClick() {
                return false;
            }

            @Override
            public boolean isDown() {
                return isKeyDown();
            }

            @Override
            public int getBoundCode() {
                return key.value();
            }

            @Override
            public KeyBinding refs() {
                return this;
            }
        };
    }

    @Override
    public KeyBinding getBinding() {
        return keyBinding;
    }

}
