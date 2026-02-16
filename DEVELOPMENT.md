# Setting up a local development

## Build secrets

Build secrets are now set up via a `secret-config.toml` file located in the root
directory. Note that by default, this file is marked as ignored for version control.

A sample template can be found at [`secret-config.toml.template`](./secret-config.toml.template).

This config file is a [TOML file](https://toml.io/en/), with the following expected format:

```toml
[signing]
keystore-password = "<root password>"
keystore-alias = "<alias name>"
keystore-alias-password = "<alias password>"
```

Or:

```toml
signing.keystore-password = "<root password>"
signing.keystore-alias = "<alias name>"
signing.keystore-alias-password = "<alias password>"
```

This file **must** be specified or the build config will otherwise attempt to use the
following environment variables:

 TOML key | Environment variable equivalent
---|---
`signing.keystore-password` | `APP_KEYSTORE_PASSWORD`
`signing.keystore-alias` | `APP_KEYSTORE_ALIAS`
`signing.keystore-alias-password` | `APP_KEYSTORE_ALIAS_PASSWORD`

### Migrating from the old properties configuration file

Using the old `.properties` file format is no longer supported in favour
of TOML, which (in my opinion) is better for specifying other additional properties if desired.

An example old file migrated to the TOML config is as follows:

`secret_keys.properties` (before):

```properties
keystore_password=password123
keystore_alias=Release key
keystore_alias_password=aliaspassword
```

`secret-config.toml` (after):

```toml
[signing]
keystore-password = "password123"
keystore-alias = "Release key"
keystore-alias-password = "aliaspassword"
```
