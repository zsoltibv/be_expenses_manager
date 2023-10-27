--
-- Insert default currencies
--

INSERT IGNORE INTO `currency` (`code`)
VALUES
  ('RON'),
  ('USD'),
  ('EUR');

--
-- Insert default categories
--

INSERT IGNORE INTO `category` (`description`, `color`)
VALUES
  ('Food & Restaurants', '#5338F5'),
  ('Car', '#1EB3DB'),
  ('Entertainment', '#FF8084'),
  ('Subscriptions', '#E83FAB'),
  ('Coffee', '#76A305'),
  ('House Hold', '#17C99E'),
  ('Bank Statement', '#F5A623');
