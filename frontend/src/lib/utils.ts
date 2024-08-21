import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

class Cookie {
  private name: string;

  constructor(name: string) {
    this.name = name;
  }

  get() {
    return document.cookie
      .split(";")
      .find((c) => c.trim().startsWith(`${this.name}=`))
      ?.split("=")[1];
  }

  set(value: string, expires?: Date) {
    const expirationDate =
      expires || new Date(Date.now() + 2 * 7 * 24 * 60 * 60 * 1000); // 2 weeks from now
    document.cookie = `${
      this.name
    }=${value}; expires=${expirationDate.toUTCString()}; path=/;`;
  }
}

class CookieManager {
  public authToken = new Cookie("authToken");
}

export const cookies = new CookieManager();