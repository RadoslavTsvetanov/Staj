import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}



class Cookie{ // Note: having name here and later in the cookie manager is a but redundant but it gets the job done 
    private name: string
    constructor(name: string) {
        this.name = name
    }

    get() {
        return document.cookie.split(';').find(c => c.trim().startsWith(`${this.name}=`))?.split('=')[1]
    }

    set(value: string, expires?: Date) {
        document.cookie = `${this.name}=${value}${expires? `; expires=${expires.toUTCString()}` : ''}`
    }

}
class CookieManager{
    public authToken = new Cookie("authToken")
}

export const cookies = new CookieManager() 
// --------------------------


class Api{ // we will generate it from the open api docs from spring so dont do it manually

}