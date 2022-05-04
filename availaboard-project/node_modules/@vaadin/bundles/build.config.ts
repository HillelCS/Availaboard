/**
 * Configuration for the bundles build
 */

/**
 * Modules directory for the bundles build. If building in a workspace,
 * set to workspace root.
 */
export const modulesDirectory = './node_modules';

export const exposePackages = {
  /**
   * The listed packages are not exposed in the bundle, although they may
   * be bundled as dependencies.
   */
  exclude: [
    // NOTE: Lit libraries are excluded to allow installing and using different
    // versions (possibly older versions installed by addons) simultaniously.
    // See: https://github.com/vaadin/flow-components/issues/2950
    'lit-html',
    'lit-element',
    '@lit/reactive-element'
  ]
};