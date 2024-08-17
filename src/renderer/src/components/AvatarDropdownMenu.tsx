import { DropdownItem, DropdownMenu } from '@nextui-org/react'

/**
 *
 * @returns JSX.Element
 */
const AvatarDropdownMenu = (): React.ReactElement => {
  const preferencesClicked: React.MouseEventHandler<HTMLLIElement> = (): void => {
    
  }
  return (
    <DropdownMenu aria-label="Profile Actions" variant="flat">
      <DropdownItem key="profile" disableAnimation className="h-14">
        <p className="font-semibold">Signed in as</p>
        <p className="font-semibold">john.doe@example.com</p>
      </DropdownItem>
      {/** Preferences is shown for every user. And is set to default settings*/}
      <DropdownItem key="settings" onClick={preferencesClicked}>
        Preferences
      </DropdownItem>
      {/** Analytics should only be shown for users with admin permissions. */}
      <DropdownItem key="analytics">Analytics</DropdownItem>
      {/** System should only be shown for admins */}
      <DropdownItem key="system">System</DropdownItem>
      {/** Re-direct to repo github issues */}
      <DropdownItem key="help_and_feedback">Help & Feedback</DropdownItem>
      <DropdownItem key="logout" color="danger">
        Log Out
      </DropdownItem>
    </DropdownMenu>
  )
}

export default AvatarDropdownMenu
