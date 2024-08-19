import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
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
    // Default to 2 weeks if no expiration date is provided
    const expirationDate =
      expires || new Date(Date.now() + 2 * 7 * 24 * 60 * 60 * 1000); // 2 weeks from now
    document.cookie = `${
      this.name
    }=${value}; expires=${expirationDate.toUTCString()}; path=/;`;
  }
}

class CookieManager{
    public authToken = new Cookie("authToken")
}

export const cookies = new CookieManager() 
// --------------------------


class Api{ // we will generate it from the open api docs from spring so dont do it manually

}