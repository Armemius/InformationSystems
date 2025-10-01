export type UserRole = "ROLE_USER" | "ROLE_ADMIN"

export default interface User {
  username: string;
  token: string;
  role: UserRole;
}
