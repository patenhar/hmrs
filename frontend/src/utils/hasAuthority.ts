export const hasAuthority = (authorities, required) => {
  if (!authorities) return false;
  if (Array.isArray(required)) {
    return required.some((r) => authorities.includes(r));
  }
  return authorities.includes(required);
};
