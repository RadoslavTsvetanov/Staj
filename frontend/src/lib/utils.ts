import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";

// Function to merge and conditionally apply class names
export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

// Utility class to handle cookies
class Cookie {
  private name: string;

  constructor(name: string) {
    this.name = name;
  }

  // Get the value of the cookie
  get() {
    const value = document.cookie
      .split("; ")
      .find((cookie) => cookie.startsWith(`${this.name}=`))
      ?.split("=")[1];

    // Decode the cookie value to handle special characters
    return value ? decodeURIComponent(value) : undefined;
  }

  // Set the value of the cookie
  set(value: string, expires?: Date) {
    const expirationDate = expires || new Date(Date.now() + 2 * 7 * 24 * 60 * 60 * 1000); // 2 weeks from now
    document.cookie = `${this.name}=${encodeURIComponent(value)}; expires=${expirationDate.toUTCString()}; path=/;`;
  }

  // Delete the cookie by setting its expiration date to the past
  delete() {
    document.cookie = `${this.name}=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/;`;
  }
}

// Manager class to handle multiple cookies
class CookieManager {
  public authToken = new Cookie("authToken");
}

// Export the cookie manager instance
export const cookies = new CookieManager();
