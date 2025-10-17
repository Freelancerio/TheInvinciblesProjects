import { renderHook, act, waitFor } from "@testing-library/react";
import { UserProvider, UserContext } from "../UserContext";
import { useContext } from "react";
import getBaseUrl from "../api";

jest.mock("../api");

// Mock firebase auth
jest.mock("firebase/auth", () => ({
    getAuth: jest.fn(() => ({
        onAuthStateChanged: jest.fn()
    })),
    onAuthStateChanged: jest.fn()
}));

const mockFirebaseUser = {
    uid: "firebase-uid-123",
    email: "test@example.com",
    getIdToken: jest.fn().mockResolvedValue("mock-id-token")
};

const mockBackendUser = {
    firebaseId: "firebase-uid-123",
    email: "test@example.com",
    username: "testuser"
};

describe("UserContext", () => {
    beforeEach(() => {
        getBaseUrl.mockReturnValue("http://localhost:8080");
        localStorage.clear();
        global.fetch = jest.fn();
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    test("provides default user context value", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result } = renderHook(() => useContext(UserContext), { wrapper });

        expect(result.current.user).toBeNull();
        expect(typeof result.current.setUser).toBe("function");
    });

    test("allows updating user through setUser", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result } = renderHook(() => useContext(UserContext), { wrapper });

        act(() => {
            result.current.setUser(mockBackendUser);
        });

        expect(result.current.user).toEqual(mockBackendUser);
    });

    test("can set user to null", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result } = renderHook(() => useContext(UserContext), { wrapper });

        act(() => {
            result.current.setUser(mockBackendUser);
        });

        expect(result.current.user).toEqual(mockBackendUser);

        act(() => {
            result.current.setUser(null);
        });

        expect(result.current.user).toBeNull();
    });

    test("provides user state to multiple consumers", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;

        const { result: result1 } = renderHook(() => useContext(UserContext), { wrapper });
        const { result: result2 } = renderHook(() => useContext(UserContext), { wrapper });

        act(() => {
            result1.current.setUser(mockBackendUser);
        });

        expect(result1.current.user).toEqual(mockBackendUser);
    });

    test("user context value persists across renders", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result, rerender } = renderHook(() => useContext(UserContext), { wrapper });

        act(() => {
            result.current.setUser(mockBackendUser);
        });

        rerender();

        expect(result.current.user).toEqual(mockBackendUser);
    });

    test("can update user partially", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result } = renderHook(() => useContext(UserContext), { wrapper });

        act(() => {
            result.current.setUser({ firebaseId: "test-123" });
        });

        expect(result.current.user).toEqual({ firebaseId: "test-123" });

        act(() => {
            result.current.setUser({ ...result.current.user, email: "new@example.com" });
        });

        expect(result.current.user).toEqual({
            firebaseId: "test-123",
            email: "new@example.com"
        });
    });

    test("handles multiple user updates", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result } = renderHook(() => useContext(UserContext), { wrapper });

        const users = [
            { firebaseId: "user1", email: "user1@example.com" },
            { firebaseId: "user2", email: "user2@example.com" },
            { firebaseId: "user3", email: "user3@example.com" }
        ];

        users.forEach(user => {
            act(() => {
                result.current.setUser(user);
            });
            expect(result.current.user).toEqual(user);
        });
    });

    test("user starts as null", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result } = renderHook(() => useContext(UserContext), { wrapper });

        expect(result.current.user).toBeNull();
    });

    test("setUser function reference remains stable", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result, rerender } = renderHook(() => useContext(UserContext), { wrapper });

        const firstSetUser = result.current.setUser;

        act(() => {
            result.current.setUser(mockBackendUser);
        });

        rerender();

        expect(result.current.setUser).toBe(firstSetUser);
    });

    test("can handle user with all properties", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result } = renderHook(() => useContext(UserContext), { wrapper });

        const fullUser = {
            firebaseId: "firebase-123",
            email: "test@example.com",
            username: "testuser",
            displayName: "Test User",
            photoURL: "https://example.com/photo.jpg"
        };

        act(() => {
            result.current.setUser(fullUser);
        });

        expect(result.current.user).toEqual(fullUser);
    });

    test("can handle empty object as user", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result } = renderHook(() => useContext(UserContext), { wrapper });

        act(() => {
            result.current.setUser({});
        });

        expect(result.current.user).toEqual({});
    });

    test("context updates trigger re-renders", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        let renderCount = 0;

        const { result } = renderHook(() => {
            renderCount++;
            return useContext(UserContext);
        }, { wrapper });

        const initialRenderCount = renderCount;

        act(() => {
            result.current.setUser(mockBackendUser);
        });

        expect(renderCount).toBeGreaterThan(initialRenderCount);
    });

    test("can clear user after being set", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result } = renderHook(() => useContext(UserContext), { wrapper });

        act(() => {
            result.current.setUser(mockBackendUser);
        });

        expect(result.current.user).not.toBeNull();

        act(() => {
            result.current.setUser(null);
        });

        expect(result.current.user).toBeNull();
    });

    test("handles rapid user updates", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result } = renderHook(() => useContext(UserContext), { wrapper });

        act(() => {
            result.current.setUser({ firebaseId: "user1" });
            result.current.setUser({ firebaseId: "user2" });
            result.current.setUser({ firebaseId: "user3" });
        });

        expect(result.current.user).toEqual({ firebaseId: "user3" });
    });

    test("user object can contain nested properties", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result } = renderHook(() => useContext(UserContext), { wrapper });

        const userWithNested = {
            firebaseId: "test-123",
            email: "test@example.com",
            profile: {
                firstName: "Test",
                lastName: "User",
                preferences: {
                    theme: "dark",
                    notifications: true
                }
            }
        };

        act(() => {
            result.current.setUser(userWithNested);
        });

        expect(result.current.user).toEqual(userWithNested);
        expect(result.current.user.profile.firstName).toBe("Test");
        expect(result.current.user.profile.preferences.theme).toBe("dark");
    });

    test("preserves user data types", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result } = renderHook(() => useContext(UserContext), { wrapper });

        const userWithTypes = {
            firebaseId: "test-123",
            email: "test@example.com",
            age: 25,
            isActive: true,
            balance: 100.50,
            tags: ["premium", "verified"],
            metadata: { lastLogin: new Date("2024-01-15") }
        };

        act(() => {
            result.current.setUser(userWithTypes);
        });

        expect(typeof result.current.user.age).toBe("number");
        expect(typeof result.current.user.isActive).toBe("boolean");
        expect(typeof result.current.user.balance).toBe("number");
        expect(Array.isArray(result.current.user.tags)).toBe(true);
        expect(result.current.user.metadata.lastLogin instanceof Date).toBe(true);
    });

    test("multiple UserProviders can exist independently", () => {
        const wrapper1 = ({ children }) => <UserProvider>{children}</UserProvider>;
        const wrapper2 = ({ children }) => <UserProvider>{children}</UserProvider>;

        const { result: result1 } = renderHook(() => useContext(UserContext), { wrapper: wrapper1 });
        const { result: result2 } = renderHook(() => useContext(UserContext), { wrapper: wrapper2 });

        act(() => {
            result1.current.setUser({ firebaseId: "user1" });
        });

        act(() => {
            result2.current.setUser({ firebaseId: "user2" });
        });

        expect(result1.current.user).toEqual({ firebaseId: "user1" });
        expect(result2.current.user).toEqual({ firebaseId: "user2" });
    });

    test("setUser handles undefined gracefully", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result } = renderHook(() => useContext(UserContext), { wrapper });

        act(() => {
            result.current.setUser(undefined);
        });

        expect(result.current.user).toBeUndefined();
    });

    test("context value structure is correct", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result } = renderHook(() => useContext(UserContext), { wrapper });

        expect(result.current).toHaveProperty("user");
        expect(result.current).toHaveProperty("setUser");
    });

    test("can update specific user properties", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result } = renderHook(() => useContext(UserContext), { wrapper });

        act(() => {
            result.current.setUser({
                firebaseId: "test-123",
                email: "test@example.com",
                username: "testuser"
            });
        });

        act(() => {
            result.current.setUser({
                ...result.current.user,
                username: "newusername"
            });
        });

        expect(result.current.user.username).toBe("newusername");
        expect(result.current.user.firebaseId).toBe("test-123");
        expect(result.current.user.email).toBe("test@example.com");
    });

    test("handles user logout scenario", () => {
        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;
        const { result } = renderHook(() => useContext(UserContext), { wrapper });

        // Login
        act(() => {
            result.current.setUser(mockBackendUser);
        });

        expect(result.current.user).not.toBeNull();

        // Logout
        act(() => {
            result.current.setUser(null);
        });

        expect(result.current.user).toBeNull();
    });

    test("user state is isolated per provider instance", () => {
        const TestComponent1 = () => {
            const { user, setUser } = useContext(UserContext);
            return { user, setUser };
        };

        const TestComponent2 = () => {
            const { user, setUser } = useContext(UserContext);
            return { user, setUser };
        };

        const wrapper = ({ children }) => <UserProvider>{children}</UserProvider>;

        const { result: result1 } = renderHook(() => TestComponent1(), { wrapper });
        const { result: result2 } = renderHook(() => TestComponent2(), { wrapper });

        act(() => {
            result1.current.setUser(mockBackendUser);
        });

        expect(result1.current.user).toEqual(mockBackendUser);
    });
});