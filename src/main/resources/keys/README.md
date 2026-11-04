# JWT signing keys (RS256)

The **public** key (`jwt-public.pem`) is committed — every service needs it to
verify tokens. The **private** key (`jwt-private.pem`) is intentionally **not**
committed (it is git-ignored) because these repositories are public and a
private signing key must never be published.

## Generate a dev key pair (run once per machine)

From this directory (`src/main/resources/keys`), using Git Bash (ships with
OpenSSL on Windows):

```bash
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out jwt-private.pem
openssl rsa -in jwt-private.pem -pubout -out jwt-public.pem
```

Then copy the **public** key so the verifier services trust tokens you sign:

```bash
cp jwt-public.pem ../../../../../order-engine/src/main/resources/keys/jwt-public.pem
cp jwt-public.pem ../../../../../ai-recipe-service/src/main/resources/keys/jwt-public.pem
```

## Production

Override `jwt.private-key` / `jwt.public-key` to point at mounted secrets; never
ship a private key inside the build.
